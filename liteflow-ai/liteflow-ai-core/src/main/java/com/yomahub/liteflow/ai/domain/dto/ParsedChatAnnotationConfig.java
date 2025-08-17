package com.yomahub.liteflow.ai.domain.dto;

import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;

import java.util.ArrayList;
import java.util.List;

/**
 * AIChat 注解解析后的配置类
 *
 * @author 苍镜月
 * @since TODO
 */

public class ParsedChatAnnotationConfig extends ParsedAnnotationConfig {

    private boolean streaming = true;
    private TransportType transportType = TransportType.SSE;
    private List<String> toolNames = new ArrayList<>();

    public boolean isStreaming() {
        return streaming;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public List<String> getToolNames() {
        return toolNames;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public void setToolNames(List<String> toolNames) {
        this.toolNames = toolNames;
    }
}
