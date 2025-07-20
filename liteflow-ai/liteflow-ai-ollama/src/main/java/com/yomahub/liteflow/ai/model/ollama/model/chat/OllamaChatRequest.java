package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.util.request.RequestBody;
import com.yomahub.liteflow.ai.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.message.Message;

import java.util.List;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaChatRequest extends ChatRequest {

    private static final String OLLAMA_MESSAGES_KEY = "prompt";

    private String prompt;

    public OllamaChatRequest() {
        super();
    }

    public OllamaChatRequest(
            String prompt,
            List<Message> messages,
            ChatOptions options,
            TransportListener transportListener,
            ResultHandler resultHandler,
            ChunkCallbackTransformer chunkCallbackTransformer
    ) {
        super(messages, options, transportListener, resultHandler, chunkCallbackTransformer);
        this.prompt = prompt;
    }

    public OllamaChatRequest(Builder builder) {
        super(builder);
        this.prompt = builder.prompt;
    }

    @Override
    public RequestBody toRequestBody() {
        return super.toRequestBody()
                .put(OLLAMA_MESSAGES_KEY, prompt);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ChatRequest.Builder<Builder> {
        private String prompt;

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

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
