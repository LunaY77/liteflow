package com.yomahub.liteflow.ai.engine.model;

/**
 * 大模型响应
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface ModelResponse<T> {

    T getOutput();
}
