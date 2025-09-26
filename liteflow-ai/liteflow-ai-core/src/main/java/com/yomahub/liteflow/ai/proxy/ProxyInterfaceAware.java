package com.yomahub.liteflow.ai.proxy;

/**
 * 代理对象原始接口感知接口, 作为包内部接口(package-private)，保证外部无法获取，从而标识 AI 组件的动态代理对象。
 *
 * @param <T> 原始接口类型
 * @author 苍镜月
 * @since 2.16.0
 */
interface ProxyInterfaceAware<T> {

    /**
     * 获取动态代理对象所代理的原始接口的 Class 类
     *
     * @return 原始接口 Class
     */
    Class<T> getProxiedInterface();
}
