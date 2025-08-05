package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;

import java.time.Duration;
import java.util.Map;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaChatConfig extends ChatConfig {

    public OllamaChatConfig() {
        super();
    }

    public OllamaChatConfig(
            String apiUrl,
            String endPoint,
            String apiKey,
            String provider,
            String model,
            Duration timeout,
            Map<String, Object> headersConfig,
            boolean autoToolCallEnabled,
            boolean streaming,
            TransportType transportType
    ) {
        super(apiUrl, endPoint, apiKey, provider, model, timeout,
                headersConfig, autoToolCallEnabled, streaming, transportType);
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

    public OllamaChatConfig(Builder builder) {
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
        public OllamaChatConfig build() {
            checkRequiredFields();
            return new OllamaChatConfig(this);
        }
    }
}
