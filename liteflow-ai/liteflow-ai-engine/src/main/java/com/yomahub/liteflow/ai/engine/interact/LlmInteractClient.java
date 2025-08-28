package com.yomahub.liteflow.ai.engine.interact;

import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformerFactory;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.ToolMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 大模型交互客户端，统筹消息传输、协议转换等功能。
 *
 * @author 苍镜月
 * @since TODO
 */

public class LlmInteractClient implements InteractClient {

    private static final EngineLog LOG = EngineLogManager.getLogger(LlmInteractClient.class);

    @Override
    public void stream(ChatConfig config, ChatRequest request) {
        InteractManager manager = new InteractManager(config, request);
        manager.executeStreaming();
    }

    @Override
    public ChatResponse chat(ChatConfig config, ChatRequest request) {
        InteractManager interactManager = new InteractManager(config, request);
        return interactManager.executeBlocking();
    }

    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatConfig config, ChatRequest request) {
        CompletableFuture<ChatResponse> future = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                future.complete(chat(config, request));
            } catch (Exception e) {
                future.completeExceptionally(new LiteFlowAIEngineException("异步调用大模型失败", e));
            }
        });

        return future;
    }

    /**
     * 内部执行器
     */
    private static class InteractManager {
        private final ChatConfig config;
        private final ChatRequest request;
        private final InteractContext context;
        private final ChunkProcessPipeline pipeline;
        private final TransportListener externalTransportListener;
        private final ResultHandler resultHandler;
        private final Transport transport;
        private final InternalTransportListener internalTransportListener;

        public InteractManager(ChatConfig config, ChatRequest request) {
            this.config = config;
            this.request = request;
            this.context = new InteractContext();
            ProtocolTransformer protocolTransformer = ProtocolTransformerFactory.getTransformer(config.getProvider());
            this.pipeline = request.isStreaming()
                    ? ChunkProcessPipeline.createStreamingPipeline(context, protocolTransformer, request.getChunkCallbackTransformer())
                    : ChunkProcessPipeline.createBlockingPipeline(context, protocolTransformer);
            this.transport = TransportType.getTransportInstance(request.getTransportType());
            this.externalTransportListener = request.getTransportListener();
            this.resultHandler = request.getResultHandler();
            this.internalTransportListener = new InternalTransportListener();
        }

        /**
         * 流式调用
         */
        public void executeStreaming() {
            // 启动传输，使用内部监听器
            transport.start(config, request, pipeline, internalTransportListener);
        }

        /**
         * 内部传输监听器，用于处理流式调用的各种事件
         */
        private class InternalTransportListener implements TransportListener {

            @Override
            public void onStart(InteractContext context) {
                externalTransportListener.onStart(context);
            }

            @Override
            public void onClose(InteractContext context) {
                ChatResponse finalResponse = null;
                // 判断是否需要继续进行工具调用
                boolean isContinuingWithToolCall = false;
                try {
                    // 构造最终响应
                    finalResponse = pipeline.buildFinalStreamingResponse();

                    // 调用结果处理器的完成回调
                    finalResponse = resultHandler.onCompletion(finalResponse, context);

                    // 工具调用
                    if (finalResponse.hasToolCalls() && config.isAutoToolCallEnabled()) {
                        // 1. 获取并执行工具调用
                        List<ToolCall> toolCalls = finalResponse.getOutput().getToolCalls();
                        // 目前只执行单轮单次的工具调用
                        ToolMessage toolMessage = executeToolCall(toolCalls.get(0), request.getToolRegistry());

                        // 2. 构建下一轮对话的上下文
                        buildNextRoundMessages(request, finalResponse, toolMessage);

                        // 3. 递归调用下一轮消息
                        // 设置标志位为 true，避免关闭 transport
                        isContinuingWithToolCall = true;
                        new InteractManager(config, request).executeStreaming();
                    }

                } catch (Exception e) {
                    onError(context, e);
                } finally {
                    // 不需要进行工具调用时才进行清理操作
                    if (!isContinuingWithToolCall) {
                        try {
                            // 调用外部监听器的关闭事件
                            externalTransportListener.onClose(context);
                        } catch (Exception e) {
                            onError(context, e);
                        } finally {
                            // 清理资源
                            cleanup(finalResponse);
                        }
                    }
                    // 如果需要进行工具调用，将清理的责任委托给下一轮调用
                }
            }

            @Override
            public void onError(InteractContext context, Throwable t) {
                externalTransportListener.onError(context, t);
            }
        }

        public ChatResponse executeBlocking() {
            ChatResponse response = null;
            try {
                externalTransportListener.onStart(context);

                response = transport.startBlocking(config, request, pipeline);

                response = resultHandler.onCompletion(response, context);

                // 处理工具调用
                if (response.hasToolCalls() && config.isAutoToolCallEnabled()) {
                    // 1. 获取并执行工具调用
                    List<ToolCall> toolCalls = response.getOutput().getToolCalls();
                    // 目前只执行单轮单次的工具调用
                    ToolMessage toolMessage = executeToolCall(toolCalls.get(0), request.getToolRegistry());

                    // 2. 构建下一轮对话的上下文
                    buildNextRoundMessages(request, response, toolMessage);

                    // 3. 递归调用下一轮消息
                    response = new InteractManager(config, request).executeBlocking();
                }

                return response;
            } catch (Exception e) {
                this.externalTransportListener.onError(context, e);
                return response;
            } finally {
                cleanup(response);
            }
        }

        /**
         * 执行工具调用
         *
         * @param toolCall     工具调用信息
         * @param toolRegistry 工具注册中心
         * @return 工具调用结果
         */
        private ToolMessage executeToolCall(ToolCall toolCall, ToolRegistry toolRegistry) {
            // 找到对应的工具回调
            ToolCallBack toolCallBack = toolRegistry.getAllTools()
                    .stream()
                    .filter(tool -> Objects.equals(tool.getName(), tool.getName()))
                    .findFirst()
                    .orElseThrow(() ->
                            new LiteFlowAIEngineException("Unable to find target tool with tool name: " + toolCall.getName()));
            // 调用工具
            String toolResult = toolCallBack.call(toolCall.getArguments());
            // 返回工具调用结果
            return new ToolMessage(toolResult, toolCall.getId(), toolCall.getName());
        }

        /**
         * 工具调用之后构建下一轮的消息列表
         *
         * @param request     原始请求
         * @param response    大模型响应（要求工具调用）
         * @param toolMessage 工具调用结果
         */
        private void buildNextRoundMessages(ChatRequest request, ChatResponse response, ToolMessage toolMessage) {
            List<Message> messagesHistory = request.getMessages();
            messagesHistory.add(response.getOutput());
            messagesHistory.add(toolMessage);
        }

        /**
         * 清理资源
         */
        private void cleanup(ChatResponse response) {
            try {
                resultHandler.onFinal(response, context);
            } catch (Exception e) {
                LOG.error("ResultHandler.onFinal 执行失败: {}", e.getMessage());
            } finally {
                transport.close();
            }
        }
    }
}
