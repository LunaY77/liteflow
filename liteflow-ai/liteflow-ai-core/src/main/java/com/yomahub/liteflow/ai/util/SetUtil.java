package com.yomahub.liteflow.ai.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

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
     * @param consumer 要执行的操作。
     * @param value    要检查的值。
     * @param <T>      值的类型。
     */
    public static <T> void setIfPresent(Consumer<T> consumer, T value) {
        if (!isNullOrEmptyOrDefault(value)) {
            consumer.accept(value);
        }
    }

    /**
     * 检查给定的对象是否为 null 或 "空" 或 默认值
     * 如果一个对象是 String、Collection、Map 或数组，且其元素个数或长度为零，
     * 那么它被认为是"空"的。
     * 如果一个对象是 TriState 类型，并且其值为 UNSET，那么认为它是默认值
     *
     * @param value 要检查的对象。
     * @return 如果对象为 null 或 “空” 或 默认值，则返回 true，否则返回 false。
     */
    private static boolean isNullOrEmptyOrDefault(Object value) {
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
        }
        return false;
    }
}
