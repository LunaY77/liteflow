package com.yomahub.liteflow.ai.workflow.coze.util;

import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.util.KeyValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.yomahub.liteflow.ai.util.SetUtil.isTemplate;
import static com.yomahub.liteflow.ai.util.SetUtil.resolveContextExpression;

/**
 * KeyValue 转换工具类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class KeyValueUtil {

    /**
     * 将 KeyValue 注解数组转换为 Map, 并解析其中的上下文表达式
     */
    public static Map<String, Object> buildObjectMapFromKeyValue(KeyValue[] kvs, ProcessorContext<?> context) {
        Map<String, Object> map = new HashMap<>();
        for (KeyValue kv : kvs) {
            Object resolvedValue = isTemplate(kv.value()) ?
                    resolveContextExpression(kv.value(), context) :
                    kv.value();

            if (Objects.nonNull(resolvedValue)) {
                map.put(kv.key(), resolvedValue);
            }
        }
        return map;
    }

    /**
     * 将 KeyValue 注解数组转换为 Map, 并解析其中的上下文表达式
     */
    public static Map<String, String> buildStringMapFromKeyValue(KeyValue[] kvs, ProcessorContext<?> context) {
        Map<String, String> map = new HashMap<>();
        for (KeyValue kv : kvs) {
            String resolvedValue = isTemplate(kv.value()) ?
                    resolveContextExpression(kv.value(), context) :
                    kv.value();

            if (Objects.nonNull(resolvedValue)) {
                map.put(kv.key(), resolvedValue);
            }
        }
        return map;
    }
}
