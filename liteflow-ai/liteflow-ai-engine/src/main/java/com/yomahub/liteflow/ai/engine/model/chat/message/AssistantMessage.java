package com.yomahub.liteflow.ai.engine.model.chat.message;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;

import java.util.List;

/**
 * 大模型消息
 *
 * @author 苍镜月
 * @since TODO
 */

@JsonPropertyOrder({"role", "content"})
public class AssistantMessage extends AbstractMessage {

    /**
     * 工具调用列表
     */
    private List<ToolCall> toolCalls;

    public AssistantMessage(String textContent) {
        super(MessageType.ASSISTANT, textContent);
    }

    public AssistantMessage(String textContent, List<ToolCall> toolCalls) {
        super(MessageType.ASSISTANT, textContent);
        this.toolCalls = toolCalls;
    }

    public List<ToolCall> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }
}
