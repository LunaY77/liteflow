package com.yomahub.liteflow.ai.context;

import com.yomahub.liteflow.slot.DefaultContext;

import java.util.UUID;

/**
 * chat 上下文, 用于存储 StreamHandler
 * <p>
 * 对于 StreamHandler 参数，可以通过流程参数传入，也可以通过 ChatContext 的构造函数传入
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatContext extends DefaultContext {

    private String chatId;

    private StreamHandler streamHandler;

    public ChatContext() {
        this.chatId = "chat_" + UUID.randomUUID();
        this.streamHandler = null;
    }

    public ChatContext(StreamHandler streamHandler) {
        this.chatId = "chat_" + UUID.randomUUID();
        this.streamHandler = streamHandler;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public StreamHandler getStreamHandler() {
        return streamHandler;
    }

    public void setStreamHandler(StreamHandler streamHandler) {
        this.streamHandler = streamHandler;
    }
}
