package com.yomahub.liteflow.ai.tool;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.ToolDefinition;
import com.yomahub.liteflow.ai.engine.tool.annotation.Tool;
import com.yomahub.liteflow.ai.engine.tool.annotation.ToolParam;
import com.yomahub.liteflow.ai.engine.tool.method.MethodToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对于 Spring 环境下的工具注册中心实现，用户可以将 Tool 注册为 Spring Bean 从而被该类自动发现与注册。
 *
 * @author 苍镜月
 * @since TODO
 */

public class SpringBeanToolRegistry implements ToolRegistry {

    private static final LFLog LOG = LFLoggerManager.getLogger(SpringBeanToolRegistry.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ApplicationContext applicationContext;
    private final Map<String, ToolCallBack> toolCache = new ConcurrentHashMap<>();

    // 使用 volatile 修饰，禁用指令重排
    private volatile boolean allToolsLoaded = false;

    public SpringBeanToolRegistry(ApplicationContext applicationContext) {
        Objects.requireNonNull(applicationContext, "ApplicationContext must not be null");
        this.applicationContext = applicationContext;
    }

    /**
     * 获取工具回调 （懒加载）
     *
     * @param toolName 工具名称
     * @return 工具实例
     */
    @Override
    public ToolCallBack getTool(String toolName) {
        return toolCache.computeIfAbsent(toolName, name -> {
            // 扫描 bean 寻找匹配的 @Tool 方法
            for (String beanName : applicationContext.getBeanDefinitionNames()) {
                Object bean = applicationContext.getBean(beanName);
                // 扫描 Bean 中的所有方法
                for (Method method : bean.getClass().getMethods()) {
                    if (method.isAnnotationPresent(Tool.class)) {
                        Tool toolAnnotation = method.getAnnotation(Tool.class);
                        // 获取工具名称，优先使用注解中的名称，否则使用方法名
                        String currToolName = StrUtil.isNotBlank(toolAnnotation.name()) ?
                                toolAnnotation.name() : method.getName();
                        // 匹配
                        if (Objects.equals(currToolName, toolName)) {
                            return createToolCallBack(toolName, bean, method, toolAnnotation);
                        }
                    }
                }
            }
            LOG.info("Tool '{}' not found in Spring context", toolName);
            return null;
        });
    }

    @Override
    public Collection<ToolCallBack> getAllTools() {
        // 懒加载，所以如果没有全部初始化，需要主动进行加载
        if (!allToolsLoaded) {
            synchronized (this) {
                // 双端检锁，避免重复初始化
                if (!allToolsLoaded) {
                    for (String beanName : applicationContext.getBeanDefinitionNames()) {
                        Object bean = applicationContext.getBean(beanName);
                        // 扫描 Bean 中的所有方法
                        for (Method method : bean.getClass().getMethods()) {
                            if (method.isAnnotationPresent(Tool.class)) {
                                Tool toolAnnotation = method.getAnnotation(Tool.class);
                                // 获取工具名称，优先使用注解中的名称，否则使用方法名
                                String toolName = StrUtil.isNotBlank(toolAnnotation.name()) ?
                                        toolAnnotation.name() : method.getName();
                                toolCache.computeIfAbsent(toolName, name -> createToolCallBack(name, bean, method, toolAnnotation));
                            }
                        }
                    }
                }
                allToolsLoaded = true;
                LOG.info("Full scan for tools completed, total tools found: {}", toolCache.size());
            }
        }
        return new ArrayList<>(toolCache.values());
    }

    /**
     * 创建 ToolCallBack 实现
     *
     * @param toolName       工具名
     * @param bean           Spring Bean 实例
     * @param method         方法实例
     * @param toolAnnotation 工具注解实例
     * @return ToolCallBack 实现
     */
    // TODO 动态代理 Bean
    private ToolCallBack createToolCallBack(String toolName, Object bean, Method method, Tool toolAnnotation) {
        // 1. 获取工具描述
        String description = String.join("\n", toolAnnotation.value());

        // 2. 动态生成 JsonSchema
        JsonNode schema;
        if (method.getParameterCount() == 1 && !method.getParameterTypes()[0].isPrimitive()) {
            // 如果方法参数只有一个非原始类型的参数，则直接使用该参数的类型作为 schema
            Type paramType = method.getGenericParameterTypes()[0];
            schema = JsonSchemaGenerator.generate(paramType);
        } else {
            // 多个参数，提取为 Map，最后聚合为 Schema
            Map<String, Type> typeMap = extractMethodParamsAsTypeMap(method);
            schema = JsonSchemaGenerator.generateFromTypeMap(typeMap, true);
        }

        // 3. 创建 ToolDefinition
        ToolDefinition<?> toolDefinition = new ToolDefinition<>(toolName, description, schema);

        // 4. 创建 ToolCallBack
        return new MethodToolCallBack(toolDefinition, bean, method);
    }

    /**
     * 提取方法参数名称和类型
     *
     * @param method 方法实例
     * @return 参数名和类型的映射
     */
    private Map<String, Type> extractMethodParamsAsTypeMap(Method method) {
        Map<String, Type> paramsMap = new LinkedHashMap<>();
        for (Parameter parameter : method.getParameters()) {
            ToolParam toolParam = parameter.getAnnotation(ToolParam.class);

            String paramName;
            // 这里将注解的 value 作为参数名，如果注解不存在，那么将使用方法参数的名字
            // 但是需要开启 -parameters 参数，否则可能无法获取到
            if (Objects.nonNull(toolParam) && StrUtil.isNotBlank(toolParam.value())) {
                paramName = toolParam.value();
            } else {
                paramName = parameter.getName();
            }
            paramsMap.put(paramName, parameter.getParameterizedType());
        }
        return paramsMap;
    }
}
