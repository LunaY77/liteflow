package com.yomahub.liteflow.ai.engine.tool.registry;

import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;

import java.util.Collection;

/**
 * 工具调用注册接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ToolRegistry {

    /**
     * 获取工具
     *
     * @param toolName 工具名称
     * @return 工具实例
     */
    ToolCallBack getTool(String toolName);

    /**
     * 获取所有注册的工具
     *
     * @return 所有工具的集合
     */
    Collection<ToolCallBack> getAllTools();
}
