package com.yomahub.liteflow.ai.util;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.parse.context.ContextAccessor;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class SetUtil {

    /**
     * 占位符正则表达式 {{变量名}}
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");

    /**
     * 如果表达式不为空且解析结果不为空，则调用消费者
     */
    public static void setIfPresent(Consumer<String> consumer, String expression, ProcessorContext<?> context) {
        if (isPresent(expression)) {
            String value = isTemplate(expression) ?
                    resolveContextExpression(expression, context) :
                    expression;
            if (StrUtil.isNotBlank(value)) {
                consumer.accept(value);
            }
        }
    }

    /**
     * 如果表达式不为空且解析结果不为空，则调用消费者
     */
    public static <Res> void setIfPresent(Consumer<Res> consumer, String expression, ProcessorContext<?> context, Class<Res> type) {
        setIfPresent(consumer, expression, context, type, null);
    }

    /**
     * 如果表达式不为空且解析结果不为空，则调用消费者
     *
     * @param defaultValue 默认值转换器，当表达式不是模板时生效
     */
    public static <Res> void setIfPresent(Consumer<Res> consumer, String expression, ProcessorContext<?> context, Class<Res> type, Function<String, Res> defaultValue) {
        if (isPresent(expression)) {
            if (isTemplate(expression)) {
                Res value = resolveContextExpression(expression, context);
                if (Objects.nonNull(value)) {
                    consumer.accept(value);
                }
            } else {
                if (Objects.nonNull(defaultValue)) {
                    consumer.accept(defaultValue.apply(expression));
                }
            }
        }
    }

    /**
     * 判断是否为模板表达式
     *
     * @param expression 表达式
     * @return 是否为模板
     */
    public static boolean isTemplate(String expression) {
        if (StrUtil.isBlank(expression)) {
            return false;
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(expression);
        return matcher.find();
    }

    /**
     * 解析上下文表达式，上下文表达式通过 {{变量名}} 形式引用上下文中的变量
     *
     * @param expression 表达式
     * @param context    处理上下文
     * @param <Res>      结果类型
     * @return 解析结果
     */
    public static <Res> Res resolveContextExpression(String expression, ProcessorContext<?> context) {
        if (StrUtil.isBlank(expression)) {
            return null;
        }

        // 解析
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(expression);

        if (matcher.find()) {
            String placeholder = matcher.group(1).trim();
            return ContextAccessor.searchContextByExpression(placeholder, context);
        }
        return null;
    }

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
