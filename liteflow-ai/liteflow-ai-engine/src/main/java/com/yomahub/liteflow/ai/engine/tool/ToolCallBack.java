package com.yomahub.liteflow.ai.engine.tool;

/**
 * 工具调用接口，封装了工具的定义和执行逻辑
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface ToolCallBack {

    /**
     * 获取工具名称
     *
     * @return 工具名称
     */
    String getName();

    /**
     * 获取工具定义
     *
     * @return 工具定义对象，包含名称、描述和输入参数的 JSON Schema
     */
    ToolDefinition<?> getDefinition();

    /**
     * 执行工具调用
     *
     * @param input json 格式的输入
     * @return json 格式的输出
     */
    String call(String input);
}
