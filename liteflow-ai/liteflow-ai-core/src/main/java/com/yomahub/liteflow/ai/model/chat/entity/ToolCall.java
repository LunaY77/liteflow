package com.yomahub.liteflow.ai.model.chat.entity;

import java.util.Map;

/**
 * 工具调用
 *
 * @author 苍镜月
 * @since TODO
 */
public class ToolCall {

    /**
     * 工具 id
     */
    private String id;

    /**
     * 工具名
     */
    private String name;

    /**
     * 工具参数
     */
    private Map<String, Object> args;

}