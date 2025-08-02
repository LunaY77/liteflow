package com.yomahub.liteflow.ai.engine.model.chat.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yomahub.liteflow.ai.engine.model.chat.entity.ToolCall;

/**
 * 大模型消息
 *
 * @author 苍镜月
 * @since TODO
 */
public class AssistantMessage extends AbstractMessage {

    /**
     * 工具调用列表
     */
    private List<ToolCall> toolCalls;

    private AssistantMessage(String textContent, Map<String, Object> metadata) {
        super(MessageType.ASSISTANT, textContent, metadata);
    }

    private AssistantMessage(String textContent, List<ToolCall> toolCalls, Map<String, Object> metadata) {
        super(MessageType.ASSISTANT, textContent, metadata);
        this.toolCalls = toolCalls;
    }

    public AssistantMessage(String textContent) {
        this(textContent, new HashMap<>());
    }

    public AssistantMessage(String textContent, List<ToolCall> toolCalls) {
        this(textContent, toolCalls, new HashMap<>());
    }

    public List<ToolCall> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }
}
