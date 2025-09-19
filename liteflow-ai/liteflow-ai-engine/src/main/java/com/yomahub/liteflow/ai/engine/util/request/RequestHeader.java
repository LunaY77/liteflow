package com.yomahub.liteflow.ai.engine.util.request;

import java.util.Map;

/**
 * HTTP请求头
 *
 * @author 苍镜月
 * @since TODO
 */

public class RequestHeader extends HttpPartBuilder<RequestHeader, Map<String, String>> {

    public RequestHeader() {
        super();
    }

    public RequestHeader(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected RequestHeader self() {
        return this;
    }

    @Override
    public Map<String, String> convert() {
        return toStringMap();
    }

    /**
     * 创建一个空的 RequestHeader 实例的静态工厂方法。
     *
     * @return 一个新的空 {@code RequestHeader} 实例。
     */
    public static RequestHeader of() {
        return new RequestHeader();
    }

    /**
     * 使用一个已有的 Map 创建 RequestHeader 实例的静态工厂方法。
     *
     * @param data 初始数据 Map。
     * @return 一个包含初始数据的新 {@code RequestHeader} 实例。
     */
    public static RequestHeader of(Map<String, Object> data) {
        return new RequestHeader(data);
    }

    /**
     * 创建一个包含单个键值对的 RequestHeader 实例的静态工厂方法。
     * <p>
     * 这是创建并初始化实例的一种便捷方式。
     *
     * @param key   初始键。
     * @param value 初始值。
     * @return 一个包含初始键值对的新 {@code RequestHeader} 实例。
     */
    public static RequestHeader of(String key, Object value) {
        return new RequestHeader().put(key, value);
    }
}
