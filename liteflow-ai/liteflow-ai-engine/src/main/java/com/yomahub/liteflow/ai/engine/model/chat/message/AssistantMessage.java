package com.yomahub.liteflow.ai.engine.model.chat.message;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;

import java.util.List;

/**
 * 大模型消息
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@JsonPropertyOrder({"role", "content", "tool_calls"})
public class AssistantMessage extends AbstractMessage {

    /**
     * 工具调用列表
     */
    @JsonProperty("tool_calls")
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

    /**
     * 获取不包含 <think> 标签内容的消息内容
     *
     * @return 清理后的消息内容
     */
    @JsonIgnore
    public String getContentWithoutThink() {
        String content = this.getContent();
        if (StrUtil.isBlank(content)) {
            return "";
        }
        return content.replaceAll("(?s)<think>.*?</think>", "").trim();
    }
}
