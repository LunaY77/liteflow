package com.yomahub.liteflow.ai.engine.model.chat.message;

/**
 * 系统消息
 *
 * @author 苍镜月
 * @since TODO
 */

public class SystemMessage extends AbstractMessage {

    public SystemMessage(String textContent) {
        super(MessageType.SYSTEM, textContent);
    }

}
