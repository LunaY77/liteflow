package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.model.ModelRequest;
import com.yomahub.liteflow.ai.model.chat.message.Message;

import java.util.List;

/**
 * Chat 请求体
 *
 * @author 苍镜月
 * @since TODO
 */
public class ChatRequest implements ModelRequest {

    /**
     * 消息列表
     */
    private List<Message> messages;

    /**
     * 聊天选项配置
     */
    private ChatOptions options;

    public ChatRequest(List<Message> messages, ChatOptions options) {
        this.messages = messages;
        this.options = options;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public ChatOptions getOptions() {
        return options;
    }

    public void setOptions(ChatOptions options) {
        this.options = options;
    }

}
