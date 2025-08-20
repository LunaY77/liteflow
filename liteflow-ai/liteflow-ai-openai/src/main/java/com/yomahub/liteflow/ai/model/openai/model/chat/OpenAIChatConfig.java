package com.yomahub.liteflow.ai.model.openai.model.chat;

import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;

import java.time.Duration;
import java.util.Map;

/**
 * OpenAI 聊天配置类
 *
 * @author 苍镜月
 * @since TODO
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
            boolean autoToolCallEnabled
    ) {
        super(apiUrl, endPoint, apiKey, provider, model, connectTimeout, readTimeout, headersConfig, autoToolCallEnabled);
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
            checkRequiredFields();
            return new OpenAIChatConfig(this);
        }
    }
}
