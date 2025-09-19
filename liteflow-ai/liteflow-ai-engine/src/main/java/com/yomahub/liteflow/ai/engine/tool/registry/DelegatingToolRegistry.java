package com.yomahub.liteflow.ai.engine.tool.registry;

import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;

import java.util.*;

/**
 * 工具调用注册的委托实现类。
 * 如果存在多个工具注册器，这个类可以将它们组合起来，
 *
 * @author 苍镜月
 * @since TODO
 */

public class DelegatingToolRegistry implements ToolRegistry {

    private final List<ToolRegistry> registries;

    public DelegatingToolRegistry(List<ToolRegistry> registries) {
        this.registries = registries;
    }

    @Override
    public ToolCallBack getTool(String toolName) {
        return registries.stream()
                .map(registry -> registry.getTool(toolName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<ToolCallBack> getAllTools() {
        Map<String, ToolCallBack> allTools = new HashMap<>();
        for (ToolRegistry registry : registries) {
            registry.getAllTools().forEach(tool -> allTools.putIfAbsent(tool.getName(), tool));
        }
        return allTools.values();
    }
}
