package com.yomahub.liteflow.ai.model.chat.message;

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
    MessageType getMessageType();

}
