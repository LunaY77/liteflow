package com.yomahub.liteflow.ai.model.chat.stream;

/**
 * 流式处理
 *
 * @author 苍镜月
 * @since TODO
 */

public interface StreamHandler {

    void onStart();

    void onMessage();

    void onStop();

    void onError();
}
