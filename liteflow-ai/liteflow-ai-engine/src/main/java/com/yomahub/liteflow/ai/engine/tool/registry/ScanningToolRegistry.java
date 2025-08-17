package com.yomahub.liteflow.ai.engine.tool.registry;

import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自动扫描注册工具
 *
 * @author 苍镜月
 * @since TODO
 */

public class ScanningToolRegistry implements ToolRegistry {

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
        // TODO
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
