package com.yomahub.liteflow.ai.interact.protocol;

import org.springframework.beans.factory.InitializingBean;

/**
 * 消息协议转换器自动注册
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class ProtocolTransformerRegistrar implements InitializingBean, ProtocolTransformer {

    @Override
    public void afterPropertiesSet() throws Exception {
        register();
    }

    private void register() {
        ProtocolTransformerFactory.registerTransformer(getProviderName(), this);
    }

    /**
     * 获取大模型提供者名称
     *
     * @return 大模型提供者名称
     */
    protected abstract String getProviderName();

}
