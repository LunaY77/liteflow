package com.yomahub.liteflow.ai.model.chat.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统消息
 *
 * @author 苍镜月
 * @since TODO
 */

public class SystemMessage extends AbstractMessage {

    private SystemMessage(String textContent, Map<String, Object> metadata) {
        super(MessageType.SYSTEM, textContent, metadata);
    }

    public SystemMessage(String textContent) {
        this(textContent, new HashMap<>());
    }
}
