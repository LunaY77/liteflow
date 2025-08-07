package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;

import java.util.List;

/**
 * TODO
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
            TransportListener transportListener,
            ResultHandler resultHandler,
            ChunkCallbackTransformer chunkCallbackTransformer
    ) {
        super(messages, options, transportListener, resultHandler, chunkCallbackTransformer);
    }

    public OllamaChatRequest(Builder builder) {
        super(builder);
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
