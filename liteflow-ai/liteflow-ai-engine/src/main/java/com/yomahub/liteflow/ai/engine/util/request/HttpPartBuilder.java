package com.yomahub.liteflow.ai.engine.util.request;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 创建 Http 请求体和请求头的构建器
 * 四大功能
 * 1. merge
 * 2. convert
 * 3. put
 * 4. remove
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class HttpPartBuilder<T extends HttpPartBuilder<T, R>, R> {

    protected final Map<String, Object> data;

    public HttpPartBuilder() {
        this.data = new LinkedHashMap<>();
    }

    public HttpPartBuilder(Map<String, Object> data) {
        this.data = new LinkedHashMap<>(data);
    }

    /**
     * 返回具体构建器的当前实例。
     * 用于支持流畅的链式方法调用。
     *
     * @return 当前实例
     */
    protected abstract T self();

    /**
     * 将另一个构建器的数据合并到当前构建器中。
     *
     * @param other 另一个 HttpPartBuilder 实例。
     * @return 当前构建器实例，用于链式调用。
     */
    public T merge(T other) {
        if (Objects.nonNull(other)) {
            this.data.putAll(other.data);
        }
        return self();
    }

    /**
     * 转化为具体的请求体或请求头。
     * 这个方法需要在子类中实现，以便将内部数据转换为特定的格式。
     *
     * @return {@link String} or {@link Map}
     */
    public abstract R convert();

    /**
     * 请求体转化使用
     * 将内部数据转换为格式化的JSON字符串。
     *
     * @return 一个格式化的JSON字符串。
     */
    protected String toJsonString() {
        return JSON.toJSONString(this.data, JSONWriter.Feature.PrettyFormat);
    }

    /**
     * 请求头转换使用
     * 将内部数据转换为 Map<String, String></String,>
     *
     * @return map
     */
    protected Map<String, String> toStringMap() {
        Map<String, String> stringMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : this.data.entrySet()) {
            stringMap.put(entry.getKey(), Objects.toString(entry.getValue(), ""));
        }
        return stringMap;
    }

    /**
     * 从数据映射中移除一个键。
     *
     * @param key 要移除的键。
     * @return 当前构建器实例，用于链式调用。
     */
    public T remove(String key) {
        this.data.remove(key);
        return self();
    }

    /**
     * 从数据映射中批量移除多个键。
     *
     * @param keys 要移除的键列表。
     * @return 当前构建器实例，用于链式调用。
     */
    public T removeBatch(String... keys) {
        for (String key : keys) {
            this.data.remove(key);
        }
        return self();
    }

    /**
     * 清空映射中的所有数据。
     *
     * @return 当前构建器实例，用于链式调用。
     */
    public T removeAll() {
        this.data.clear();
        return self();
    }

    /**
     *向数据映射中添加一个键值对。
     *
     * @param key   键。
     * @param value 值。
     * @return 当前构建器实例，用于链式调用。
     */
    public T put(String key, Object value) {
        this.data.put(key, value);
        return self();
    }

    /**
     * 当值不为"空"时，添加一个键值对。
     *
     * @param key   键。
     * @param value 要检查并添加的值。
     * @return 当前构建器实例，用于链式调用。
     */
    public T putIfNotEmpty(String key, Object value) {
        if (isPresent(value)) {
            this.data.put(key, value);
        }
        return self();
    }

    /**
     * 当值不为 null 时，添加一个键值对。
     *
     * @param key   键。
     * @param value 要检查并添加的值。
     * @return 当前构建器实例，用于链式调用。
     */
    public T putIfNotNull(String key, Object value) {
        if (isPresent(value)) {
            this.data.put(key, value);
        }
        return self();
    }

    /**
     * 当给定条件为 true 时，添加一个键值对。
     *
     * @param condition 控制是否添加的布尔条件。
     * @param key       键。
     * @param value     值。
     * @return 当前构建器实例，用于链式调用。
     */
    public T putIf(boolean condition, String key, Object value) {
        if (condition) {
            this.data.put(key, value);
        }
        return self();
    }

    private static boolean isPresent(Object value) {
        return !isNotPresent(value);
    }

    /**
     * 检查给定的对象是否为 null 或 "空"。
     * 如果一个对象是 String、Collection、Map 或数组，且其元素个数或长度为零，
     * 那么它被认为是"空"的。
     *
     * @param value 要检查的对象。
     * @return 如果对象为 null 或空，则返回 true，否则返回 false。
     */
    private static boolean isNotPresent(Object value) {
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
