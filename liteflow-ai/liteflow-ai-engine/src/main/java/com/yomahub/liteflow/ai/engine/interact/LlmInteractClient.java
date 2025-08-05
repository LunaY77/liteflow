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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        try {
            return chatAsync(config, request).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new LiteFlowAIEngineException("同步调用大模型失败", e.getCause());
        }
    }

    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatConfig config, ChatRequest request) {
        CompletableFuture<ChatResponse> future = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                InteractManager manager = new InteractManager(config, request);
                ChatResponse response = manager.executeBlocking();
                future.complete(response);
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
            this.pipeline = config.isStreaming()
                    ? ChunkProcessPipeline.createStreamingPipeline(context, protocolTransformer, request.getChunkCallbackTransformer())
                    : ChunkProcessPipeline.createBlockingPipeline(context, protocolTransformer);
            this.transport = TransportType.getTransportInstance(config.getTransportType());
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
                try {
                    // 构造最终响应
                    finalResponse = pipeline.buildFinalStreamingResponse();

                    // 调用结果处理器的完成回调
                    finalResponse = resultHandler.onCompletion(finalResponse, context);

                    // TODO: 工具调用

                    // 调用外部监听器的关闭事件
                    externalTransportListener.onClose(context);
                } catch (Exception e) {
                    onError(context, e);
                } finally {
                    // 清理资源
                    cleanup(finalResponse);
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

                // TODO 工具调用

                return response;
            } catch (Exception e) {
                this.externalTransportListener.onError(context, e);
                return response;
            } finally {
                cleanup(response);
            }
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
