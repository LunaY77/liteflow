package com.yomahub.liteflow.ai.model;

import org.springframework.beans.factory.InitializingBean;

/**
 * 运行时自动注册器
 * 实现类继承此类实现自动注册大模型运行时
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class ModelRuntimeRegistrar implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        register();
    }

    /**
     * 注册大模型运行时
     */
    private void register() {
        ModelRuntimeFactory.registerModelProvider(getProviderName(), getRuntimeClass());
    }

    /**
     * 获取大模型提供者名称
     *
     * @return 大模型提供者名称
     */
    protected abstract String getProviderName();

    /**
     * 获取大模型运行时类
     *
     * @return 大模型运行时类
     */
    protected abstract Class<? extends BaseModel<? extends ModelConfig>> getRuntimeClass();
}
