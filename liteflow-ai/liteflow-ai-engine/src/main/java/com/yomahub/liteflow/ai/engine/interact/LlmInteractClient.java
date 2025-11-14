package com.yomahub.liteflow.ai.engine.interact;

import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.interact.chunk.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformerFactory;
import com.yomahub.liteflow.ai.engine.interact.chunk.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.ToolMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 大模型交互客户端
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class LlmInteractClient implements InteractClient {

    private static final EngineLog LOG = EngineLogManager.getLogger(LlmInteractClient.class);

    /**
     * 同步调用
     *
     * @param config  聊天配置
     * @param request 聊天请求
     * @return 聊天响应
     */
    @Override
    public ChatResponse chat(ChatConfig config, ChatRequest request) {
        ProtocolTransformer protocolTransformer = ProtocolTransformerFactory.getTransformer(config.getProvider());
        Transport transport = request.getTransportType().getTransportInstance();

        ChatResponse response; // 用于存储最终响应

        while (true) {
            InteractContext context = new InteractContext();

            // 1. 执行单轮对话
            String responseBody = transport.startBlocking(config, request);
            response = protocolTransformer.transformBlockingResponse(responseBody, context);

            // 2. 检查循环的 "退出条件"
            // 如果 AI 没有要求工具调用，或者配置禁用了自动调用，
            // 那么这就是最终答案，跳出循环。
            if (!response.hasToolCalls() || !config.isAutoToolCallEnabled()) {
                break;
            }

            // 3. 获取并执行工具调用
            List<ToolCall> toolCalls = response.getOutput().getToolCalls();
            // 目前只执行单轮单次的工具调用
            ToolMessage toolMessage = request.getToolRegistry().executeToolCall(toolCalls.get(0));

            // 4. 构建下一轮对话的上下文
            buildNextRoundMessages(request, response, toolMessage);
        }

        // 5. 返回循环中断时的最后一个响应
        return response;
    }

    /**
     * 异步调用
     *
     * @param config  聊天配置
     * @param request 聊天请求
     * @return 异步聊天响应
     */
    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatConfig config, ChatRequest request) {
        return CompletableFuture.supplyAsync(() -> chat(config, request));
    }

    /**
     * 创建响应式流事件管道
     *
     * @param config  聊天配置
     * @param request 聊天请求
     * @return 包含 ChunkEvent 的流
     */
    public Flowable<ChunkEvent> stream(ChatConfig config, ChatRequest request) {
        InteractContext context = new InteractContext();
        ProtocolTransformer protocolTransformer = ProtocolTransformerFactory.getTransformer(config.getProvider());

        return Flowable.just(ChunkEvent.start(context))
                .concatWith(
                        streamRecursive(config, request, context, protocolTransformer)
                );
    }

    /**
     * 内部递归流逻辑
     *
     * @param config              聊天配置
     * @param request             聊天请求
     * @param context             当前轮次的交互上下文
     * @param protocolTransformer 协议转换器
     * @return 包含 ChunkEvent 的流
     */
    private Flowable<ChunkEvent> streamRecursive(ChatConfig config, ChatRequest request,
                                                 InteractContext context, ProtocolTransformer protocolTransformer) {
        return Flowable.create(emitter -> {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            emitter.setDisposable(compositeDisposable);

            Transport transport = request.getTransportType().getTransportInstance();

            try {
                // 获取响应式流
                Disposable transportDisposable = transport.startStreaming(config, request)
                        .subscribe(
                                // onNext: 处理每个原始 JSON 分块
                                rawChunk -> {
                                    try {
                                        // 使用协议转换器转换为框架标准格式
                                        StreamingProtocolChunk protocolChunk = protocolTransformer.transformStreamingChunk(rawChunk, context);

                                        // 根据分块信息更新上下文
                                        updateContextFromChunk(context, protocolChunk);

                                        // 发送分块事件
                                        if (!emitter.isCancelled()) {
                                            emitter.onNext(ChunkEvent.chunk(rawChunk, protocolChunk, context));
                                        }
                                    } catch (Exception e) {
                                        if (!emitter.isCancelled()) {
                                            emitter.onError(e); // 转发解析错误
                                        }
                                    }
                                },
                                // onError: 转发下游错误
                                error -> {
                                    if (!emitter.isCancelled()) {
                                        emitter.onError(error);
                                    }
                                    try {
                                        transport.close(); // 确保关闭当前轮次的 transport
                                    } catch (Exception e) {
                                        LOG.warn("Error closing transport after onError", e);
                                    }
                                },
                                // onComplete: 流完成处理
                                () -> {
                                    try {
                                        // 构建最终响应
                                        ChatResponse finalResponse = protocolTransformer.transformStreamingResponse(context);

                                        // 检查是否需要工具调用
                                        if (finalResponse.hasToolCalls() && config.isAutoToolCallEnabled()) {
                                            if (emitter.isCancelled()) {
                                                return;
                                            }

                                            // 1. 执行工具调用
                                            List<ToolCall> toolCalls = finalResponse.getOutput().getToolCalls();
                                            ToolMessage toolMessage = request.getToolRegistry().executeToolCall(toolCalls.get(0));

                                            // 2. 构建下一轮消息
                                            buildNextRoundMessages(request, finalResponse, toolMessage);

                                            // 3. 为下一轮创建新的上下文
                                            InteractContext nextContext = new InteractContext();

                                            // 4. 递归调用 "循环体"，并将事件转发给当前 emitter
                                            Disposable recursiveDisposable = streamRecursive(config, request, nextContext, protocolTransformer)
                                                    .subscribe(
                                                            emitter::onNext,
                                                            emitter::onError,
                                                            emitter::onComplete
                                                    );

                                            compositeDisposable.add(recursiveDisposable);
                                        } else {
                                            if (!emitter.isCancelled()) {
                                                // 没有工具调用，发送 complete 事件并完成流
                                                emitter.onNext(ChunkEvent.complete(context, finalResponse));
                                                emitter.onComplete();
                                            }
                                        }
                                    } catch (Exception e) {
                                        if (!emitter.isCancelled()) {
                                            emitter.onError(e); // 转发 onComplete 逻辑中的错误
                                        }
                                    } finally {
                                        try {
                                            transport.close(); // 确保关闭当前轮次的 transport
                                        } catch (Exception e) {
                                            LOG.warn("Error closing transport", e);
                                        }
                                    }
                                }
                        );

                compositeDisposable.add(transportDisposable);
            } catch (Exception e) {
                if (!emitter.isCancelled()) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 根据分块信息更新交互上下文
     *
     * @param context 交互上下文
     * @param chunk   协议分块
     */
    private void updateContextFromChunk(InteractContext context, StreamingProtocolChunk chunk) {
        if (Objects.isNull(chunk)) {
            return;
        }

        switch (chunk.getType()) {
            case TEXT:
                context.addText((String) chunk.getData());
                break;
            case THINKING:
                context.addThinking((String) chunk.getData());
                break;
            default:
                // 其他类型暂不处理
                break;
        }
    }

    /**
     * 为下一轮对话构建消息列表
     *
     * @param request     聊天请求
     * @param response    聊天响应
     * @param toolMessage 工具调用结果
     */
    private void buildNextRoundMessages(ChatRequest request, ChatResponse response, ToolMessage toolMessage) {
        List<Message> messages = request.getMessages();
        messages.add(response.getOutput());
        messages.add(toolMessage);
    }
}
