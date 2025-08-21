package com.yomahub.liteflow.ai.engine.tool.method;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.ToolDefinition;
import com.yomahub.liteflow.ai.engine.tool.annotation.ToolParam;
import com.yomahub.liteflow.ai.engine.util.ObjectMapperHolder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 方法工具回调类
 *
 * @author 苍镜月
 * @since TODO
 */

public class MethodToolCallBack implements ToolCallBack {

    private static final EngineLog LOG = EngineLogManager.getLogger(MethodToolCallBack.class);

    private final ToolDefinition<?> toolDefinition;
    private final Object bean;
    private final Method method;

    public MethodToolCallBack(ToolDefinition<?> toolDefinition, Object bean, Method method) {
        this.toolDefinition = Objects.requireNonNull(toolDefinition, "ToolDefinition must not be null");
        this.bean = Objects.requireNonNull(bean, "Bean instance must not be null");
        this.method = Objects.requireNonNull(method, "Method must not be null");
    }

    @Override
    public String getName() {
        return this.toolDefinition.getName();
    }

    @Override
    public ToolDefinition<?> getDefinition() {
        return this.toolDefinition;
    }

    @Override
    public String call(String input) {
        try {
            Object[] args = new Object[method.getParameterCount()];

            if (method.getParameterCount() == 1 && !method.getParameterTypes()[0].isPrimitive()) {
                // 方法只有一个非原始类型参数时，尝试将输入解析为该参数类型
                Type paramType = method.getGenericParameterTypes()[0];
                args[0] = ObjectMapperHolder.readValue(input, paramType);
            } else if (method.getParameterCount() > 0) {
                // 方法有多个参数
                JsonNode inputNode = ObjectMapperHolder.readTree(input);
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    ToolParam toolParam = parameter.getAnnotation(ToolParam.class);

                    String paramName;
                    // 这里将注解的 value 作为参数名，如果注解不存在，那么将使用方法参数的名字
                    // 但是需要开启 -parameters 参数，否则可能无法获取到
                    // 如果未开启获取到的是 arg0, arg1, ... 的形式
                    if (Objects.nonNull(toolParam) && StrUtil.isNotBlank(toolParam.value())) {
                        paramName = toolParam.value();
                    } else {
                        paramName = parameter.getName();
                    }
                    JsonNode argNode = inputNode.get(paramName);
                    if (argNode.isNull()) {
                        if (Objects.nonNull(toolParam) && toolParam.required()) {
                            throw new IllegalArgumentException("Missing required parameter: " + paramName);
                        }
                        args[i] = null;
                    } else {
                        args[i] = ObjectMapperHolder.treeToValue(argNode, parameter.getType());
                    }
                }
            }

            // 反射调用方法
            Object res = method.invoke(bean, args);

            if (method.getReturnType() == void.class || method.getReturnType() == Void.class) {
                return null;
            }

            return ObjectMapperHolder.writeValueAsString(res);
        } catch (Exception e) {
            // 4. 异常处理
            Throwable cause = (e instanceof java.lang.reflect.InvocationTargetException) ? e.getCause() : e;
            LOG.error("Error calling tool '{}': {}", this.getName(), cause.getMessage(), cause);
            throw new RuntimeException("Failed to execute tool '" + this.getName() + "'. Reason: " + cause.getMessage(), cause);
        }
    }
}
