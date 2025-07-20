package com.yomahub.liteflow.ai.util.request;

import java.util.Map;

/**
 * Http 请求体
 *
 * @author 苍镜月
 * @since TODO
 */

public class RequestBody extends HttpPartBuilder<RequestBody, String> {

    public RequestBody() {
        super();
    }

    public RequestBody(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected RequestBody self() {
        return this;
    }

    @Override
    public String convert() {
        return toJsonString();
    }

    /**
     * 创建一个空的 RequestBody 实例的静态工厂方法。
     *
     * @return 一个新的空 {@code RequestBody} 实例。
     */
    public static RequestBody of() {
        return new RequestBody();
    }

    /**
     * 使用一个已有的 Map 创建 RequestBody 实例的静态工厂方法。
     *
     * @param data 初始数据 Map。
     * @return 一个包含初始数据的新 {@code RequestBody} 实例。
     */
    public static RequestBody of(Map<String, Object> data) {
        return new RequestBody(data);
    }

    /**
     * 创建一个包含单个键值对的 RequestBody 实例的静态工厂方法。
     * <p>
     * 这是创建并初始化实例的一种便捷方式。
     *
     * @param key   初始键。
     * @param value 初始值。
     * @return 一个包含初始键值对的新 {@code RequestBody} 实例。
     */
    public static RequestBody of(String key, Object value) {
        return new RequestBody().put(key, value);
    }
}
