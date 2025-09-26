package com.yomahub.liteflow.ai.engine.model.chat.message;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 系统消息
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@JsonPropertyOrder({"role", "content"})
public class SystemMessage extends AbstractMessage {

    public SystemMessage(String textContent) {
        super(MessageType.SYSTEM, textContent);
    }

}
