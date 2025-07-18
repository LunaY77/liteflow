package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.model.ModelResponse;
import com.yomahub.liteflow.ai.model.chat.message.AssistantMessage;

/**
 * chat 响应体
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatResponse implements ModelResponse {

    /**
     * 助手消息
     */
    private AssistantMessage message;

    /**
     * 聊天ID
     */
    private String chatId;

    /**
     * 是否完成
     */
    private boolean finished;

    /**
     * 使用情况统计
     */
    private Object usage;

    public ChatResponse() {
    }

    public ChatResponse(AssistantMessage message, String chatId, boolean finished) {
        this.message = message;
        this.chatId = chatId;
        this.finished = finished;
    }

    public AssistantMessage getMessage() {
        return message;
    }

    public void setMessage(AssistantMessage message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Object getUsage() {
        return usage;
    }

    public void setUsage(Object usage) {
        this.usage = usage;
    }
}
