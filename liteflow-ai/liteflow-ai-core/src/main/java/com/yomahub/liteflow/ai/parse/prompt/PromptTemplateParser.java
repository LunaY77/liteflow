package com.yomahub.liteflow.ai.parse.prompt;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.parse.context.ContextAccessor;
import com.yomahub.liteflow.core.NodeComponent;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 提示词模板解析器
 *
 * @author 苍镜月
 * @since TODO
 */

public class PromptTemplateParser {

    /**
     * 占位符正则表达式 {{变量名}}
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");

    /**
     * 处理模板占位符
     *
     * @param template      模板内容
     * @param inputFields   输入字段映射
     * @param nodeComponent 节点组件
     * @return 处理后的内容
     */
    public static String parseTemplate(String template, InputField[] inputFields, NodeComponent nodeComponent) {
        if (StrUtil.isBlank(template)) {
            return template;
        }

        // 预处理
        Map<String, InputField> fieldMap = Optional.ofNullable(inputFields)
                .map(fields ->
                        Arrays.stream(inputFields).collect(Collectors.toMap(InputField::name, Function.identity())))
                .orElse(new HashMap<>());

        // 解析 替换
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuffer res = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(1).trim();
            String value = resolveValue(placeholder, fieldMap, nodeComponent);
            matcher.appendReplacement(res, Matcher.quoteReplacement(value));
        }

        matcher.appendTail(res);
        return res.toString();
    }

    /**
     * 解析占位符值
     * 按照优先级：InputField表达式 > 直接查找 > 默认值
     *
     * @param placeholder   占位符名称
     * @param fieldMap      输入字段 Map
     * @param nodeComponent 节点组件
     * @return 解析值
     */
    private static String resolveValue(String placeholder, Map<String, InputField> fieldMap, NodeComponent nodeComponent) {
        InputField field = fieldMap.get(placeholder);
        // 第一优先级：使用 InputField 中的 expression 映射
        if (Objects.nonNull(field)) {
            // 使用表达式在上下文查找
            String value = ContextAccessor.searchContextByExpression(field.expression(), nodeComponent);
            if (StrUtil.isNotBlank(value)) {
                return value;
            }
            // 表达式未找到，使用默认值
            if (StrUtil.isNotBlank(field.defaultValue())) {
                return field.defaultValue();
            }
            // 如果是必需字段但没有找到值，抛出异常
            if (field.required()) {
                throw new LiteFlowAIException("Required field '" + placeholder + "' not found in nodeComponent and no default value provided.");
            }
        }

        // 第二优先级，占位符作为表达式在上下文中查找
        String value = ContextAccessor.searchContextByExpression(placeholder, nodeComponent);
        if (StrUtil.isNotBlank(value)) {
            return value;
        }

        // 如果都没有找到，返回原始占位符
        return placeholder;
    }
}