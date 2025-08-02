package com.yomahub.liteflow.ai.engine.model;

/**
 * 大模型标识接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface BaseModel<T extends ModelConfig> {

    /**
     * 获取大模型配置信息
     * @return 大模型配置
     */
    T getModelConfig();

}
