package com.yomahub.liteflow.ai.model.chat.message;

import com.yomahub.liteflow.ai.exception.LiteFlowAIException;

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
    protected final String textContent;

    /**
     * 元数据
     */
    protected final Map<String, Object> metadata;

    /**
     * 创建 AbstractMessage
     *
     * @param messageType 消息类型
     * @param textContent 文本内容
     * @param metadata    元数据
     */
    protected AbstractMessage(MessageType messageType, String textContent, Map<String, Object> metadata) {
        if (Objects.isNull(messageType)) {
            throw new LiteFlowAIException("消息类型不能为 null");
        }
        if (Objects.isNull(metadata)) {
            throw new LiteFlowAIException("Metadata 不能为null");
        }
        this.messageType = messageType;
        this.textContent = textContent;
        this.metadata = new HashMap<>(metadata);
        this.metadata.put(MESSAGE_TYPE, messageType);
    }

    @Override
    public Map<String, Object> getMetaData() {
        return this.metadata;
    }

    @Override
    public String getContent() {
        return this.textContent;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }
}

