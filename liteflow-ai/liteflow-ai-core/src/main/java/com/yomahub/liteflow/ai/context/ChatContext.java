package com.yomahub.liteflow.ai.context;

/**
 * chat 上下文
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatContext {

    private String chatId;

    private StreamHandler streamHandler;

    public ChatContext() {}

    public ChatContext(String chatId, StreamHandler streamHandler) {
        this.chatId = chatId;
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
