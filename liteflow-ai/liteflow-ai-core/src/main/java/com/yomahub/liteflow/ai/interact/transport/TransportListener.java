package com.yomahub.liteflow.ai.interact.transport;

import com.yomahub.liteflow.ai.interact.pipeline.ChatContext;

/**
 * 传输监听器接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface TransportListener {

    /**
     * 请求开始时的回调方法
     *
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    void onStart(ChatContext context);

    /**
     * 请求关闭时的回调方法
     *
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    void onClose(ChatContext context);

}
