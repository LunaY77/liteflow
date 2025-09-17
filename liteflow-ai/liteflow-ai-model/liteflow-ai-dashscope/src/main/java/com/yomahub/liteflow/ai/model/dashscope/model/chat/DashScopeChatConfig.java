package com.yomahub.liteflow.ai.model.dashscope.model.chat;

import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * DashScope 聊天配置类
 *
 * @author 苍镜月
 * @since TODO
 */

public class DashScopeChatConfig extends ChatConfig {

    public DashScopeChatConfig() {
        super();
    }

    public DashScopeChatConfig(
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

    public DashScopeChatConfig(Builder builder) {
        super(builder);
    }

    @Override
    protected void checkRequiredFields() {
        super.checkRequiredFields();
        Objects.requireNonNull(this.apiKey, "API Key must not be null, please set it via configuration: {liteflow.ai.dashscope.api-key}");
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
        public DashScopeChatConfig build() {
            return new DashScopeChatConfig(this);
        }
    }
}
