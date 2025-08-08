package com.yomahub.liteflow.ai.parse.context;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.AIOutput;
import com.yomahub.liteflow.ai.annotation.OutputField;
import com.yomahub.liteflow.ai.engine.model.output.Response;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.util.SetUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.util.Objects;

/**
 * 上下文访问器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ContextAccessor {

    private static final LFLog LOG = LFLoggerManager.getLogger(ContextAccessor.class);

    /**
     * 根据表达式在上下文中查找值(AIInput注解使用)
     *
     * @param expression 表达式
     * @param context    处理器上下文
     * @return 查找到的值
     */
    public static <T> T searchContextByExpression(String expression, ProcessorContext<?> context) {
        if (StrUtil.isBlank(expression)) {
            return null;
        }

        try {
            NodeComponent nodeComponent = context.getNodeComponent();
            return nodeComponent.getContextValue(expression);
        } catch (Exception e) {
            LOG.info("Failed to search context by expression: {}", expression);
            return null;
        }
    }

    /**
     * 根据表达式在上下文中设置值(AIOutput注解使用)
     *
     * @param expression 表达式
     * @param context    处理器上下文
     * @param value      值
     */
    public static void setContextValueByExpression(String expression, ProcessorContext<?> context, Object value) {
        if (StrUtil.isBlank(expression) || Objects.isNull(value)) return;

        // 检查结果是否为 Response 类型
        if (!(value instanceof Response)) {
            throw new LiteFlowAIException("AI node output value must be of type Response.");
        }
        value = ((Response<?>) value).getContent();

        NodeComponent nodeComponent = context.getNodeComponent();
        AIOutput outputAnno = context.getAiOutputAnno();

        // 检查是否使用了嵌套索引
        if (outputAnno.useKeyIndex()) {
            // 如果同时使用了 key 和 index，则抛出异常
            if (SetUtil.isPresent(outputAnno.key()) && SetUtil.isPresent(outputAnno.index())) {
                throw new LiteFlowAIException("AIOutput annotation cannot use both key and index together.");
            }
            // 如果 key 和 index 都未设置，则抛出异常
            if (SetUtil.isNotPresent(outputAnno.key()) && SetUtil.isNotPresent(outputAnno.index())) {
                throw new LiteFlowAIException("AIOutput annotation must specify either key or index when useKeyIndex is true.");
            }
            Object key = SetUtil.isPresent(outputAnno.key()) ? outputAnno.key() : outputAnno.index();
            nodeComponent.setContextValue(expression, key, value);
        } else {
            // 如果未使用嵌套索引，则直接设置值
            nodeComponent.setContextValue(expression, value);
        }

        // 处理输出字段映射
        if (SetUtil.isPresent(outputAnno.mapping())) {
            for (OutputField outputField : outputAnno.mapping()) {
                // 获取源字段值
                Object fieldValue = ReflectUtil.getFieldValue(value, outputField.sourceField());

                if (Objects.isNull(fieldValue)) {
                    continue;
                }

                // 确定目标表达式，如果未指定，直接集成 AIOutput 的主表达式
                String targetExpression = StrUtil.isNotBlank(outputField.methodExpress())
                        ? outputField.methodExpress()
                        : outputAnno.methodExpress();

                // 使用了嵌套索引
                if (outputAnno.useKeyIndex()) {
                    // 如果同时使用了 key 和 index，则抛出异常
                    if (SetUtil.isPresent(outputField.key()) && SetUtil.isPresent(outputField.index())) {
                        throw new LiteFlowAIException("OutputField annotation cannot use both key and index together.");
                    }
                    // 如果 key 和 index 都未设置，则抛出异常
                    if (SetUtil.isNotPresent(outputField.key()) && SetUtil.isNotPresent(outputField.index())) {
                        throw new LiteFlowAIException("OutputField annotation must specify either key or index when useKeyIndex is true.");
                    }
                    Object key = SetUtil.isPresent(outputField.key()) ? outputField.key() : outputField.index();
                    nodeComponent.setContextValue(targetExpression, key, fieldValue);
                } else {
                    // 未使用嵌套索引，直接设置值
                    nodeComponent.setContextValue(targetExpression, fieldValue);
                }
            }
        }
    }
}
