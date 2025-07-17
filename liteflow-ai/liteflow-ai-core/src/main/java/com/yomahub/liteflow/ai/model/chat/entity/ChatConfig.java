package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.model.ModelConfig;

import java.time.Duration;
import java.util.Map;

/**
 * 对话配置信息
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatConfig extends ModelConfig {

    private boolean autoToolCallEnabled = true;

    public ChatConfig() {}

    public ChatConfig(
            String apiUrl,
            String endPoint,
            String apiKey,
            String provider,
            String model,
            Duration timeout,
            Map<String, String> headersConfig,
            boolean autoToolCallEnabled
    ) {
        super(apiUrl, endPoint, apiKey, provider, model, timeout, headersConfig);
        this.autoToolCallEnabled = autoToolCallEnabled;
    }

    private ChatConfig(Builder builder) {
        super(builder);
        this.autoToolCallEnabled = builder.autoToolCallEnabled;
    }

    public boolean isAutoToolCallEnabled() {
        return autoToolCallEnabled;
    }

    public void setAutoToolCallEnabled(boolean autoToolCallEnabled) {
        this.autoToolCallEnabled = autoToolCallEnabled;
    }

    public static class Builder extends ModelConfig.Builder {
        private boolean autoToolCallEnabled;

        public Builder autoToolCallEnabled(boolean autoToolCallEnabled) {
            this.autoToolCallEnabled = autoToolCallEnabled;
            return this;
        }

        @Override
        public ChatConfig build() {
            return new ChatConfig(this);
        }
    }
}
