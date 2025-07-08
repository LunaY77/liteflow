package com.yomahub.liteflow.ai.model.chat.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户消息
 *
 * @author 苍镜月
 * @since TODO
 */

public class UserMessage extends AbstractMessage {

    private UserMessage(String textContent, Map<String, Object> metadata) {
        super(MessageType.USER, textContent, metadata);
    }

    public UserMessage(String textContent) {
        this(textContent, new HashMap<>());
    }

    // TODO 添加 Resource 支持

}
