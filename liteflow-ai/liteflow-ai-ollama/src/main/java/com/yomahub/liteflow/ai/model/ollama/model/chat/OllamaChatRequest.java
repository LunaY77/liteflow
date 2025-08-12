package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;

import java.util.List;

/**
 * Ollama 聊天请求体
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaChatRequest extends ChatRequest {

    public OllamaChatRequest() {
        super();
    }

    public OllamaChatRequest(
            List<Message> messages,
            ChatOptions options,
            boolean streaming,
            TransportType transportType,
            TransportListener transportListener,
            ResultHandler resultHandler,
            ChunkCallbackTransformer chunkCallbackTransformer,
            ResponseType responseType,
            TypeReference<?> targetType
    ) {
        super(messages, options, streaming, transportType,
                transportListener, resultHandler, chunkCallbackTransformer,
                responseType, targetType);
    }

    public OllamaChatRequest(Builder builder) {
        super(builder);
    }

    @Override
    protected void checkTransportConsistency() {
        super.checkTransportConsistency();
        // ollama 的流式传输不支持 SSE 格式
        if (this.streaming && this.transportType == TransportType.SSE) {
            throw new IllegalArgumentException("For streaming with Ollama, the transportType must be set to `DnJson`. " +
                    "The currently configured type `SSE` is not supported.");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ChatRequest.Builder<Builder> {

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public OllamaChatRequest build() {
            checkAndAssign();
            return new OllamaChatRequest(this);
        }
    }
}
