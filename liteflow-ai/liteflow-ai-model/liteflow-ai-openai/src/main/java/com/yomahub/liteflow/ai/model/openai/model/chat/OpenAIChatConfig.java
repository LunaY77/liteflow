package com.yomahub.liteflow.ai.model.openai.model.chat;

import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * OpenAI 聊天配置类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class OpenAIChatConfig extends ChatConfig {

    public OpenAIChatConfig() {
        super();
    }

    public OpenAIChatConfig(
            String apiUrl,
            String endPoint,
            String apiKey,
            String provider,
            String model,
            Duration connectTimeout,
            Duration readTimeout,
            Map<String, Object> headersConfig,
            boolean logRequest,
            boolean logResponse,
            boolean autoToolCallEnabled
    ) {
        super(apiUrl, endPoint, apiKey, provider, model, connectTimeout, readTimeout, headersConfig, logRequest, logResponse, autoToolCallEnabled);
    }

    @Override
    protected void checkRequiredFields() {
        super.checkRequiredFields();
        Objects.requireNonNull(this.apiKey, "API Key must not be null, please set it via configuration: {liteflow.ai.openai.api-key}");
    }

    public OpenAIChatConfig(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ChatConfig.Builder<Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public OpenAIChatConfig build() {
            return new OpenAIChatConfig(this);
        }
    }
}
