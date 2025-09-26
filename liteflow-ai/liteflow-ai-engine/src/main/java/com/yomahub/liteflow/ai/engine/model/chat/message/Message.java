package com.yomahub.liteflow.ai.engine.model.chat.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 消息接口
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface Message extends Content {

    /**
     * 获取消息类型
     * @return 消息类型
     */
    @JsonProperty("role")
    MessageType getMessageType();

}
