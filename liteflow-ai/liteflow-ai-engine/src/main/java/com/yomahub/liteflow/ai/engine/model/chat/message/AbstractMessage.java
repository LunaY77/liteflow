package com.yomahub.liteflow.ai.engine.model.chat.message;


import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;

import java.util.Objects;

/**
 * 抽象大模型消息
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public abstract class AbstractMessage implements Message {

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
     * 创建 AbstractMessage
     *
     * @param messageType 消息类型
     * @param content 文本内容
     */
    protected AbstractMessage(MessageType messageType, String content) {
        if (Objects.isNull(messageType)) {
            throw new LiteFlowAIEngineException("消息类型不能为 null");
        }
        this.messageType = messageType;
        this.content = content;
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

