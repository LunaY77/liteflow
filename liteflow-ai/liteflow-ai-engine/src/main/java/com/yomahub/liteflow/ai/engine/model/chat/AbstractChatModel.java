package com.yomahub.liteflow.ai.engine.model.chat;

import com.yomahub.liteflow.ai.engine.interact.InteractClient;
import com.yomahub.liteflow.ai.engine.interact.LlmInteractClient;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象聊天模型类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public abstract class AbstractChatModel implements ChatModel {

    protected final ChatConfig config;
    protected final InteractClient interactClient;

    public AbstractChatModel(ChatConfig config) {
        this.config = config;
        this.interactClient = new LlmInteractClient();
    }

    public AbstractChatModel(ChatConfig config, InteractClient interactClient) {
        this.config = config;
        this.interactClient = interactClient;
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

    public static abstract class Builder<B extends Builder<B>> {

        protected InteractClient interactClient;
        protected abstract ChatConfig.Builder<?> getConfigBuilder();

        public Builder() {
            this.interactClient = new LlmInteractClient();
        }

        public B interactClient(InteractClient interactClient) {
            this.interactClient = interactClient;
            return self();
        }

        public B apiUrl(String apiUrl) {
            getConfigBuilder().apiUrl(apiUrl);
            return self();
        }

        public B endPoint(String endPoint) {
            getConfigBuilder().endPoint(endPoint);
            return self();
        }

        public B apiKey(String apiKey) {
            getConfigBuilder().apiKey(apiKey);
            return self();
        }

        public B provider(String provider) {
            getConfigBuilder().provider(provider);
            return self();
        }

        public B model(String model) {
            getConfigBuilder().model(model);
            return self();
        }

        public B connectTimeout(Duration connectTimeout) {
            getConfigBuilder().connectTimeout(connectTimeout);
            return self();
        }

        public B readTimeout(Duration readTimeout) {
            getConfigBuilder().readTimeout(readTimeout);
            return self();
        }

        public B headersConfig(Map<String, Object> headersConfig) {
            getConfigBuilder().headersConfig(headersConfig);
            return self();
        }

        public B autoToolCallEnabled(boolean autoToolCallEnabled) {
            getConfigBuilder().autoToolCallEnabled(autoToolCallEnabled);
            return self();
        }

        public B logRequest(boolean logRequest) {
            getConfigBuilder().logRequest(logRequest);
            return self();
        }

        public B logResponse(boolean logResponse) {
            getConfigBuilder().logResponse(logResponse);
            return self();
        }

        public abstract B self();

        public abstract ChatModel build();
    }
}
