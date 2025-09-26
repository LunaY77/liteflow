package com.yomahub.liteflow.ai.engine.tool.registry;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.ToolDefinition;
import com.yomahub.liteflow.ai.engine.tool.annotation.Tool;
import com.yomahub.liteflow.ai.engine.tool.method.MethodToolCallBack;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 自动扫描注册工具
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ScanningToolRegistry implements ToolRegistry {

    private final EngineLog LOG = EngineLogManager.getLogger(ScanningToolRegistry.class);
    private final Map<String, ToolCallBack> registry = new ConcurrentHashMap<>();

    /**
     * 使用指定的包路径扫描并注册工具
     *
     * @param packages 要扫描的包路径
     */
    public ScanningToolRegistry(String... packages) {
        scanAndRegisterTools(packages);
    }

    /**
     * 扫描指定包路径下的工具类并注册到注册表中。
     *
     * @param packages 要扫描的包路径列表
     */
    private void scanAndRegisterTools(String... packages) {
        if (Objects.isNull(packages) || packages.length == 0) {
            LOG.warn("No packages specified for tool scanning. Skipping tool registration.");
            return;
        }

        // 扫描指定包路径下的所有类
        Set<Class<?>> classes = Arrays.stream(packages)
                .map(ClassUtil::scanPackage)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        // 实例缓存
        Map<Class<?>, Object> instanceCache = new HashMap<>();

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    try {
                        // 获取实例
                        Object instance = instanceCache.computeIfAbsent(clazz, c -> {
                            try {
                                // 需要提供无参构造
                                return c.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                throw new LiteFlowAIEngineException("Failed to create instance of class: " + c.getName(), e);
                            }
                        });

                        // 获取工具注解
                        Tool toolAnnotation = method.getAnnotation(Tool.class);
                        String toolName = StrUtil.isNotBlank(toolAnnotation.name()) ?
                                toolAnnotation.name() : method.getName();
                        // 尝试注册工具
                        registry.computeIfAbsent(toolName, name -> {
                            String description = String.join("\n", toolAnnotation.value());
                            JsonNode inputSchema = JsonSchemaGenerator.generate(method, true);
                            ToolDefinition<?> toolDefinition = new ToolDefinition<>(name, description, inputSchema);
                            LOG.info("Registered tool: {} from method: [{}]", toolName, method.getName());
                            return new MethodToolCallBack(toolDefinition, instance, method);
                        });
                    } catch (Exception e) {
                        LOG.error("Failed to register tool from method: [{}] in class: [{}]. Error: {}",
                                method.getName(), clazz.getName(), e.getMessage(), e);
                    }
                }
            }
        }
        LOG.info("Tool scanning completed. Total tools registered: {}", registry.size());
    }

    @Override
    public ToolCallBack getTool(String toolName) {
        return this.registry.get(toolName);
    }

    @Override
    public Collection<ToolCallBack> getAllTools() {
        return this.registry.values();
    }
}
