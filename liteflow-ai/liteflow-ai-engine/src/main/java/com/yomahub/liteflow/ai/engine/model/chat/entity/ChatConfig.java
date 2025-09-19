package com.yomahub.liteflow.ai.engine.model.chat.entity;

import com.yomahub.liteflow.ai.engine.model.ModelConfig;

import java.time.Duration;
import java.util.Map;

/**
 * 对话配置信息
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatConfig extends ModelConfig {

    protected boolean autoToolCallEnabled = true;

    public ChatConfig() {
        super();
    }

    public ChatConfig(
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
        super(apiUrl, endPoint, apiKey, provider, model, connectTimeout, readTimeout, headersConfig);
        this.autoToolCallEnabled = autoToolCallEnabled;
    }

    protected ChatConfig(Builder<?> builder) {
        super(builder);
        this.autoToolCallEnabled = builder.autoToolCallEnabled;
    }

    public static Builder<?> builder() {
        return new Builder.BuilderImpl();
    }

    public boolean isAutoToolCallEnabled() {
        return autoToolCallEnabled;
    }

    public void setAutoToolCallEnabled(boolean autoToolCallEnabled) {
        this.autoToolCallEnabled = autoToolCallEnabled;
    }

    public static abstract class Builder<B extends Builder<B>>
            extends ModelConfig.Builder<B> {
        protected boolean autoToolCallEnabled = true;

        public B autoToolCallEnabled(boolean autoToolCallEnabled) {
            this.autoToolCallEnabled = autoToolCallEnabled;
            return self();
        }

        private static class BuilderImpl extends Builder<BuilderImpl> {
            @Override
            protected BuilderImpl self() {
                return this;
            }

            @Override
            public ChatConfig build() {
                return new ChatConfig(this);
            }
        }
    }
}
