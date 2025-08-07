package com.yomahub.liteflow.ai.engine.model.chat.message;


import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 抽象大模型消息
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractMessage implements Message {

    public static final String MESSAGE_TYPE = "message_type";

    /**
     * 消息类型
     *
     * @see MessageType
     */
    protected final MessageType messageType;

    /**
     * 消息文本内容
     */
    protected final String content;

    /**
     * 元数据
     */
    protected final Map<String, Object> metadata;

    /**
     * 创建 AbstractMessage
     *
     * @param messageType 消息类型
     * @param content 文本内容
     * @param metadata    元数据
     */
    protected AbstractMessage(MessageType messageType, String content, Map<String, Object> metadata) {
        if (Objects.isNull(messageType)) {
            throw new LiteFlowAIEngineException("消息类型不能为 null");
        }
        if (Objects.isNull(metadata)) {
            throw new LiteFlowAIEngineException("Metadata 不能为null");
        }
        this.messageType = messageType;
        this.content = content;
        this.metadata = new HashMap<>(metadata);
        this.metadata.put(MESSAGE_TYPE, messageType);
    }

    @Override
    public Map<String, Object> getMetaData() {
        return this.metadata;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }
}

