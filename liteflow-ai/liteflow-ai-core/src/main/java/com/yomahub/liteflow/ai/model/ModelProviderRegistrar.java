package com.yomahub.liteflow.ai.model;

import org.springframework.beans.factory.InitializingBean;

/**
 * 模型提供商注册器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public abstract class ModelProviderRegistrar implements ModelProvider, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        register();
    }

    private void register() {
        ModelFactory.register(this);
    }
}
