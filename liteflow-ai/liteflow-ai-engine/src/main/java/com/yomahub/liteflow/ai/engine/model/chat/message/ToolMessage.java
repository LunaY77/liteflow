package com.yomahub.liteflow.ai.engine.model.chat.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 工具调用消息
 *
 * @author 苍镜月
 * @since TODO
 */
public class ToolMessage extends AbstractMessage {

    /**
     * 工具调用ID
     */
    private String toolCallId;

    /**
     * 工具名称
     */
    private String toolName;

    private ToolMessage(String textContent, String toolCallId, String toolName, Map<String, Object> metadata) {
        super(MessageType.TOOL, textContent, metadata);
        this.toolCallId = toolCallId;
        this.toolName = toolName;
    }

    public ToolMessage(String textContent) {
        this(textContent, null, null, new HashMap<>());
    }

    public ToolMessage(String textContent, String toolCallId, String toolName) {
        this(textContent, toolCallId, toolName, new HashMap<>());
    }

    public String getToolCallId() {
        return toolCallId;
    }

    public void setToolCallId(String toolCallId) {
        this.toolCallId = toolCallId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    // TODO 添加工具调用结果支持
}
