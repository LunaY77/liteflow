package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.engine.interact.InteractClient;
import com.yomahub.liteflow.ai.engine.interact.LlmInteractClient;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Ollama 聊天模型
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaChatModel implements ChatModel {

    private final ChatConfig config;
    private final InteractClient interactClient;

    public OllamaChatModel(ChatConfig config) {
        this.config = config;
        this.interactClient = new LlmInteractClient();
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        return interactClient.chat(config, request);
    }

    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatRequest request) {
        return interactClient.chatAsync(config, request);
    }

    @Override
    public void stream(ChatRequest request) {
        interactClient.stream(config, request);
    }

    @Override
    public ChatConfig getModelConfig() {
        return config;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final OllamaChatConfig.Builder configBuilder;

        public Builder() {
            this.configBuilder = OllamaChatConfig.builder();
        }

        public Builder apiUrl(String apiUrl) {
            this.configBuilder.apiUrl(apiUrl);
            return this;
        }

        public Builder endPoint(String endPoint) {
            this.configBuilder.endPoint(endPoint);
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.configBuilder.apiKey(apiKey);
            return this;
        }

        public Builder provider(String provider) {
            this.configBuilder.provider(provider);
            return this;
        }

        public Builder model(String model) {
            this.configBuilder.model(model);
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.configBuilder.connectTimeout(connectTimeout);
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.configBuilder.readTimeout(readTimeout);
            return this;
        }

        public Builder headersConfig(Map<String, Object> headersConfig) {
            this.configBuilder.headersConfig(headersConfig);
            return this;
        }

        public Builder autoToolCallEnabled(boolean autoToolCallEnabled) {
            this.configBuilder.autoToolCallEnabled(autoToolCallEnabled);
            return this;
        }

        public OllamaChatModel build() {
            OllamaChatConfig config = this.configBuilder.build();

            return new OllamaChatModel(config);
        }
    }
}
