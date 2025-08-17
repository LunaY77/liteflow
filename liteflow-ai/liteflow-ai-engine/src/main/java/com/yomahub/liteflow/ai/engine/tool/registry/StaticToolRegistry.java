package com.yomahub.liteflow.ai.engine.tool.registry;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 静态的工具注册
 *
 * @author 苍镜月
 * @since TODO
 */

public class StaticToolRegistry implements ToolRegistry {

    private final Map<String, ToolCallBack> registry = new ConcurrentHashMap<>();

    public StaticToolRegistry() {
    }

    public StaticToolRegistry(List<ToolCallBack> initialTools) {
        if (Objects.nonNull(initialTools)) {
            initialTools.forEach(this::register);
        }
    }

    /**
     * 手动注册一个工具
     *
     * @param toolCallBack 工具
     */
    public void register(ToolCallBack toolCallBack) {
        if (Objects.nonNull(toolCallBack) && StrUtil.isNotBlank(toolCallBack.getName())) {
            this.registry.put(toolCallBack.getName(), toolCallBack);
        }
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
