package com.yomahub.liteflow.ai.engine.model.chat.message;

/**
 * 用户消息
 *
 * @author 苍镜月
 * @since TODO
 */

public class UserMessage extends AbstractMessage {

    public UserMessage(String textContent) {
        super(MessageType.USER, textContent);
    }

}
