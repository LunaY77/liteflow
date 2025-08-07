package com.yomahub.liteflow.ai.engine.model.chat.message;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * 消息接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface Message extends Content {

    /**
     * 获取消息类型
     * @return 消息类型
     */
    @JSONField(name = "role")
    MessageType getMessageType();

}
