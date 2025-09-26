package com.yomahub.liteflow.ai.engine.interact.protocol;

/**
 * 流式消息块
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class StreamingProtocolChunk {

    private String id;

    private Object data;

    private StreamingProtocolType type;

    public void setId(String id) {
        this.id = id;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setType(StreamingProtocolType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public Object getData() {
        return data;
    }

    public StreamingProtocolType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "StreamingProtocolChunk{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", type=" + type +
                '}';
    }
}
