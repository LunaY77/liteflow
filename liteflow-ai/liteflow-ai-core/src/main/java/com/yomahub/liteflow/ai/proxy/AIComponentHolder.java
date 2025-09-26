package com.yomahub.liteflow.ai.proxy;

/**
 * 代理对象持有类, 作为包内部接口(package-private)，保证外部无法获取
 *
 * @author 苍镜月
 * @since 2.16.0
 */

class AIComponentHolder<T> implements ProxyInterfaceAware<T> {

    private final Class<T> proxiedInterface;

    AIComponentHolder(Class<T> proxiedInterface) {
        this.proxiedInterface = proxiedInterface;
    }

    @Override
    public Class<T> getProxiedInterface() {
        return proxiedInterface;
    }
}
