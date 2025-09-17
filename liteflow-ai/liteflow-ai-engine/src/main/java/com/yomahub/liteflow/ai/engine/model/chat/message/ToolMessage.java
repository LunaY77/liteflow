package com.yomahub.liteflow.ai.engine.model.chat.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 工具调用消息
 *
 * @author 苍镜月
 * @since TODO
 */

@JsonPropertyOrder({"role", "content", "tool_call_id", "tool_name"})
public class ToolMessage extends AbstractMessage {

    /**
     * 工具调用ID
     */
    @JsonProperty("tool_call_id")
    private String id;

    /**
     * 工具名称
     */
    @JsonProperty("tool_name")
    private String name;

    public ToolMessage(String toolResult, String id, String name) {
        super(MessageType.TOOL, toolResult);
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
