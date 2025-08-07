package com.yomahub.liteflow.ai.domain.dto;

import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class ParsedChatAnnotationConfig extends ParsedAnnotationConfig {

    private boolean streaming = true;
    private TransportType transportType = TransportType.SSE;

    public boolean isStreaming() {
        return streaming;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }
}
