package com.yomahub.liteflow.ai.parse.context;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.OutputField;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.output.Response;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.util.SetUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import com.yomahub.liteflow.util.LiteflowContextRegexMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 上下文访问器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ContextAccessor {

    private static final LFLog LOG = LFLoggerManager.getLogger(ContextAccessor.class);

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$(\\w+)");

    /**
     * 根据表达式在上下文中查找值(AIInput注解使用)
     *
     * @param expression 表达式
     * @param context    处理器上下文
     * @return 查找到的值
     */
    public static <T> T searchContextByExpression(String expression, ProcessorContext<?> context) {
        return searchContextByExpression(expression, context.getNodeComponent());
    }

    /**
     * 根据表达式在上下文中查找值(AIInput注解使用)
     *
     * @param expression    表达式
     * @param nodeComponent 节点组件
     * @return 查找到的值
     */
    public static <T> T searchContextByExpression(String expression, NodeComponent nodeComponent) {
        if (StrUtil.isBlank(expression)) {
            return null;
        }

        try {
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
        if (value instanceof Response) {
            // 如果 request 是 ChatRequest 且 value 是 ChatResponse，则尝试进行结构化转换
            if (context.getModelRequest() instanceof ChatRequest && value instanceof ChatResponse) {
                ChatRequest chatRequest = context.getModelRequest().toChatRequest();
                if (ResponseType.JSON.equals(chatRequest.getResponseType())) {
                    value = ((ChatResponse) value).as(chatRequest.getOutputParser());
                } else {
                    value = ((ChatResponse) value).getOutput();
                }
            } else {
                value = ((Response<?>) value).getOutput();
            }
        }

        NodeComponent nodeComponent = context.getNodeComponent();
        AIOutput outputAnno = context.getAiOutputAnno();

        // 处理主输出表达式
        executeExpression(nodeComponent, expression, value);

        // 处理输出字段映射
        if (SetUtil.isPresent(outputAnno.mapping())) {
            for (OutputField outputField : outputAnno.mapping()) {
                // 获取源字段值
                Object fieldValue = ReflectUtil.getFieldValue(value, outputField.sourceField());

                if (Objects.isNull(fieldValue)) {
                    continue;
                }

                // 获取字段映射的表达式并执行
                String fieldMethodExpress = outputField.methodExpress();
                if (StrUtil.isNotBlank(fieldMethodExpress)) {
                    executeExpression(nodeComponent, fieldMethodExpress, fieldValue);
                }
            }
        }
    }

    /**
     * 解析并执行方法表达式，将值设置到上下文中
     *
     * @param nodeComponent 组件实例
     * @param methodExpress 方法表达式 (例如: "setData(\"key\", $value)")
     * @param value         要设置的值
     */
    private static void executeExpression(NodeComponent nodeComponent, String methodExpress, Object value) {
        // 查找所有占位符
        List<String> placeholders = ReUtil.findAllGroup1(PLACEHOLDER_PATTERN, methodExpress);

        // 必须有且仅有一个占位符
        if (placeholders.isEmpty()) {
            throw new LiteFlowAIException(StrUtil.format("Illegal method expression [{}]. Must contain one placeholder variable starting with '$' (e.g., $output).", methodExpress));
        }
        if (placeholders.size() > 1) {
            throw new LiteFlowAIException(StrUtil.format("Illegal method expression [{}]. Can only contain one placeholder variable, but found {}: {}.", methodExpress, placeholders.size(), placeholders));
        }

        String placeholderVarName = placeholders.get(0);

        // 移除'$'符号，生成最终给 LiteflowContextRegexMatcher 使用的表达式
        String executionExpress = methodExpress.replace("$" + placeholderVarName, placeholderVarName);

        // 准备参数 Map
        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put(placeholderVarName, value);

        // 使用 Liteflow 工具类在上下文中查找并执行表达式
        LiteflowContextRegexMatcher.searchAndSetContext(
                nodeComponent.getSlot().getContextBeanList(),
                executionExpress,
                argsMap
        );
    }
}
