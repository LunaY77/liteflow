package com.yomahub.liteflow.ai.model;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Http 请求体，三个核心功能
 * 1. merge
 * 2. toJsonString
 * 3. put
 *
 * @author 苍镜月
 * @since TODO
 */

public class RequestBody {

    private final Map<String, Object> data;

    public RequestBody() {
        this.data = new LinkedHashMap<>();
    }

    public RequestBody(Map<String, Object> data) {
        this.data = new LinkedHashMap<>(data);
    }

    /**
     * 合并其他 RequestBody 的数据到当前 RequestBody
     *
     * @param other 其他 RequestBody 实例
     * @return 当前 RequestBody 实例
     */
    public RequestBody merge(RequestBody other) {
        if (Objects.nonNull(other)) {
            this.data.putAll(other.data);
        }
        return this;
    }

    /**
     * 将内部数据转换为 Json 字符串
     *
     * @return 格式化的 Json 字符串
     */
    public String toJsonString() {
        return JSON.toJSONString(this.data, JSONWriter.Feature.PrettyFormat);
    }

    /**
     * 创建一个空的 RequestBody 实例的静态工厂方法。
     *
     * @return 一个新的空 {@code RequestBody} 实例
     */
    public static RequestBody of() {
        return new RequestBody();
    }

    /**
     * 使用一个已有的 Map 创建 RequestBody 实例的静态工厂方法。
     *
     * @param data 初始数据 Map
     * @return 一个包含初始数据的新 {@code RequestBody} 实例
     */
    public static RequestBody of(Map<String, Object> data) {
        return new RequestBody(data);
    }

    /**
     * 创建一个包含单个键值对的 RequestBody 实例的静态工厂方法。
     * <p>
     * 这是最便捷的创建并初始化实例的方式。
     *
     * @param key   初始键
     * @param value 初始值
     * @return 一个包含初始键值对的新 {@code RequestBody} 实例
     */
    public static RequestBody of(String key, Object value) {
        return new RequestBody().put(key, value);
    }

    /**
     * 向请求体中添加一个键值对。
     *
     * @param key   键
     * @param value 值
     * @return 当前实例
     */
    public RequestBody put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    /**
     * 当值不为null的情况下，向请求体中添加一个键值对。
     *
     * @param key   键
     * @param value 要检查并添加的值
     * @return 当前实例
     */
    public RequestBody putIfNotNull(String key, Object value) {
        if (!isNullOrEmpty(value)) {
            this.data.put(key, value);
        }
        return this;
    }

    /**
     * 当值不为空的情况下，向请求体中添加一个键值对。
     *
     * @param key   键
     * @param value 要检查并添加的值
     * @return 当前实例
     */
    public RequestBody putIfNotEmpty(String key, Object value) {
        if (!isNullOrEmpty(value)) {
            this.data.put(key, value);
        }
        return this;
    }

    /**
     * 当给定条件为 true 时，向请求体中添加一个键值对。
     *
     * @param condition 控制是否添加的布尔条件
     * @param key       键
     * @param value     值
     * @return 当前实例
     */
    public RequestBody putIf(boolean condition, String key, Object value) {
        if (condition) {
            this.data.put(key, value);
        }
        return this;
    }

    /**
     * 检查给定的对象是否为 null 或 "空"。
     *
     * @param value 要检查的对象
     * @return 如果对象为 null 或空，则返回 true，否则返回 false
     */
    private static boolean isNullOrEmpty(Object value) {
        if (Objects.isNull(value)) {
            return true;
        } else if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        } else if (value instanceof Map) {
            return ((Map<?, ?>) value).isEmpty();
        } else if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
