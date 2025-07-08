package com.yomahub.liteflow.ai.model.chat.message;

import java.util.Map;

/**
 * 内容接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface Content {

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    String getContent();

    /**
     * 获取元信息
     *
     * @return 元信息
     */
    Map<String, Object> getMetaData();
}
