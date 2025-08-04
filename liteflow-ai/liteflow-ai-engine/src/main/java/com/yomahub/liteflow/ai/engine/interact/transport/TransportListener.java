package com.yomahub.liteflow.ai.engine.interact.transport;

import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;

/**
 * 传输监听器接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface TransportListener {

    static TransportListener getDefault() {
        return new TransportListener() {
            @Override
            public void onStart(InteractContext context) {
            }

            @Override
            public void onClose(InteractContext context) {
            }
        };
    }

    /**
     * 请求开始时的回调方法
     *
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    void onStart(InteractContext context);

    /**
     * 请求关闭时的回调方法
     *
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    void onClose(InteractContext context);

}
