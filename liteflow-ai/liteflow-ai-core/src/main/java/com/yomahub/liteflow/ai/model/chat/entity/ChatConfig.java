package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.interact.transport.TransportType;
import com.yomahub.liteflow.ai.model.ModelConfig;
import com.yomahub.liteflow.ai.model.RequestBody;

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

    private boolean streaming = true;

    private TransportType transportType = TransportType.SSE;

    protected static final String STREAM_KEY = "stream";

    public ChatConfig() {}

    public ChatConfig(
            String apiUrl,
            String endPoint,
            String apiKey,
            String provider,
            String model,
            Duration timeout,
            Map<String, String> headersConfig,
            boolean autoToolCallEnabled,
            boolean streaming,
            TransportType transportType
    ) {
        super(apiUrl, endPoint, apiKey, provider, model, timeout, headersConfig);
        this.autoToolCallEnabled = autoToolCallEnabled;
        this.streaming = streaming;
        this.transportType = transportType;
        checkTransportConsistency();
    }

    protected ChatConfig(Builder<?> builder) {
        super(builder);
        this.autoToolCallEnabled = builder.autoToolCallEnabled;
        this.streaming = builder.streaming;
        this.transportType = builder.transportType;
        checkTransportConsistency();
    }

    @Override
    public RequestBody toRequestBody() {
        return super.toRequestBody()
                // 默认流式，如果不需要流式输出，则设置为false
                .putIf(!streaming, STREAM_KEY, streaming);
    }

    /**
     * 检查传输类型与流式输出模式的一致性。
     * 如果流式输出模式启用但传输类型为HTTP，则抛出异常。
     * 如果阻塞式输出模式启用但传输类型不是HTTP，则抛出异常。
     */
    private void checkTransportConsistency() {
        if (this.streaming && this.transportType == TransportType.HTTP) {
            throw new IllegalArgumentException("流式输出模式启用，但不支持HTTP传输。请使用SSE或WebSocket传输。");
        } else if (!this.streaming && this.transportType != TransportType.HTTP) {
            throw new IllegalArgumentException("阻塞式输出模式启用，但传输类型不支持HTTP。请使用HTTP传输。");
        }
    }

    public boolean isAutoToolCallEnabled() {
        return autoToolCallEnabled;
    }

    public void setAutoToolCallEnabled(boolean autoToolCallEnabled) {
        this.autoToolCallEnabled = autoToolCallEnabled;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public static abstract class Builder<B extends Builder<B>>
            extends ModelConfig.Builder<B> {
        protected boolean autoToolCallEnabled = true;
        protected boolean streaming = true;
        protected TransportType transportType = TransportType.SSE;

        public B autoToolCallEnabled(boolean autoToolCallEnabled) {
            this.autoToolCallEnabled = autoToolCallEnabled;
            return self();
        }

        public B streaming(boolean streaming) {
            this.streaming = streaming;
            return self();
        }

        public B transportType(TransportType transportType) {
            this.transportType = transportType;
            return self();
        }
    }
}
