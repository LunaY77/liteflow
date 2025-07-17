package com.yomahub.liteflow.ai.interact;

import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.interact.pipeline.ChatContext;
import com.yomahub.liteflow.ai.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.interact.protocol.ProtocolTransformerFactory;
import com.yomahub.liteflow.ai.interact.transport.Transport;
import com.yomahub.liteflow.ai.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.interact.transport.TransportType;
import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 大模型交互客户端，统筹消息传输、协议转换等功能。
 *
 * @author 苍镜月
 * @since TODO
 */

public class LlmInteractClient implements InteractClient {

    private static final LFLog LOG = LFLoggerManager.getLogger(LlmInteractClient.class);

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
            throw new LiteFlowAIException("同步调用大模型失败", e.getCause());
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
                future.completeExceptionally(new LiteFlowAIException("异步调用大模型失败", e));
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
        private final ChatContext context;
        private final ChunkProcessPipeline pipeline;
        private final TransportListener transportListener;
        private final ResultHandler resultHandler;
        private final Transport transport;

        public InteractManager(ChatConfig config, ChatRequest request) {
            this.config = config;
            this.request = request;
            this.context = new ChatContext();
            ProtocolTransformer protocolTransformer = ProtocolTransformerFactory.getTransformer(config.getProvider());
            this.pipeline = config.isStreaming()
                    ? ChunkProcessPipeline.createStreamingPipeline(context, protocolTransformer, request.getChunkCallbackTransformer())
                    : ChunkProcessPipeline.createBlockingPipeline(context, protocolTransformer);
            this.transport = TransportType.getTransportInstance(config.getTransportType());
            this.transportListener = request.getTransportListener();
            this.resultHandler = request.getResultHandler();
        }

        /**
         * 流式调用
         */
        public void executeStreaming() {
//            ChatResponse response = null;
//            try {
//                transportListener.onStart(context);
//
//                transport.start(config, request, pipeline, transportListener);
//
//                transportListener.onClose();
//            } catch (Exception e) {
//                handleError(response, e);
//            } finally {
//                cleanup(response);
//            }
        }

        public ChatResponse executeBlocking() {
            ChatResponse response = null;
            try {
                transportListener.onStart(context);

                response = transport.startBlocking(config, request, pipeline);

                response = resultHandler.onCompletion(response, context);

                // TODO 工具调用

                return response;
            } catch (Exception e) {
                return handleError(response, e);
            } finally {
                cleanup(response);
            }
        }

        /**
         * 处理异常
         */
        private ChatResponse handleError(ChatResponse response, Exception e) {
            try {
                response = resultHandler.onError(response, context, e);
            } catch (Exception handlerException) {
                LOG.error("ResultHandler.onError 执行失败: {}", handlerException.getMessage());
            }

            return response;
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
