package com.yomahub.liteflow.ai.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class SetUtil {

    /**
     * 如果给定的值不为 null 或 "空" 或 默认值，则调用消费者。
     *
     * @param consumer     要执行的操作。
     * @param value        要检查的值。
     * @param defaultValue 默认值，如果 value 为 null 或 "空" 则使用此值。
     * @param <T>          值的类型。
     */
    public static <T> void setIfPresent(Consumer<T> consumer, T value, T defaultValue) {
        if (isPresent(value)) {
            consumer.accept(value);
        } else {
            consumer.accept(defaultValue);
        }
    }

    /**
     * 如果给定的值不为 null 或 "空" 或 默认值，则调用消费者。
     *
     * @param consumer 要执行的操作。
     * @param value    要检查的值。
     * @param <T>      值的类型。
     */
    public static <T> void setIfPresent(Consumer<T> consumer, T value) {
        if (isPresent(value)) {
            consumer.accept(value);
        }
    }

    /**
     * 如果给定的键值对列表不为 null 或 "空"，则调用消费者。（KeyValue 转换为 HeadersConfig 专用）
     *
     * @param consumer     设置 HeadersConfig 的消费者
     * @param keyValueList 键值对列表
     */
    public static void setIfPresent(Consumer<Map<String, Object>> consumer, List<KeyValue> keyValueList) {
        if (isPresent(keyValueList)) {
            consumer.accept(
                    keyValueList
                            .stream()
                            .collect(Collectors.toMap(KeyValue::key, KeyValue::value))
            );
        }
    }

    /**
     * 如果给定的值为 null 或 "空" 或 默认值，则调用消费者。
     *
     * @param consumer 要执行的操作。
     * @param value    要检查的值。
     * @param <T>      值的类型。
     */
    public static <T> void setIfNotPresent(Consumer<T> consumer, T value) {
        if (isNotPresent(value)) {
            consumer.accept(value);
        }
    }

    /**
     * 检查给定的对象是否不为 null 且不是 "空" 或 默认值。
     * 如果一个对象是 String、Collection、Map 或数组，且其元素个数或长度为零，
     * 那么它被认为是"空"的。
     * 如果一个对象是 TriState 类型，并且其值为 UNSET，那么认为它是默认值
     * 如果一个对象是 Integer、Long、Double 或 Float 类型，并且其值为 -1 或 -1.0，那么认为它是默认值
     *
     * @param value 要检查的对象。
     * @return 如果对象不为 null 且不是 “空” 或 默认值，则返回 true，否则返回 false。
     */
    public static boolean isPresent(Object value) {
        return !isNotPresent(value);
    }

    /**
     * 检查给定的对象是否为 null 或 "空" 或 默认值
     * 如果一个对象是 String、Collection、Map 或数组，且其元素个数或长度为零，
     * 那么它被认为是"空"的。
     * 如果一个对象是 TriState 类型，并且其值为 UNSET，那么认为它是默认值
     * 如果一个对象是 Integer、Long、Double 或 Float 类型，并且其值为 -1 或 -1.0，那么认为它是默认值
     *
     * @param value 要检查的对象。
     * @return 如果对象为 null 或 “空” 或 默认值，则返回 true，否则返回 false。
     */
    public static boolean isNotPresent(Object value) {
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
        } else if (value instanceof TriState) {
            return value == TriState.UNSET;
        } else if (value instanceof Integer) {
            return (Integer) value == -1;
        } else if (value instanceof Long) {
            return (Long) value == -1L;
        } else if (value instanceof Double) {
            return (Double) value == -1.0;
        } else if (value instanceof Float) {
            return (Float) value == -1.0f;
        }
        return false;
    }
}
