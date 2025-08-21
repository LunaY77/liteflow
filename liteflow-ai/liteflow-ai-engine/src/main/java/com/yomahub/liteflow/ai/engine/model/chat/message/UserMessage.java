package com.yomahub.liteflow.ai.engine.model.chat.message;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 用户消息
 *
 * @author 苍镜月
 * @since TODO
 */

@JsonPropertyOrder({"role", "content"})
public class UserMessage extends AbstractMessage {

    public UserMessage(String textContent) {
        super(MessageType.USER, textContent);
    }

}
