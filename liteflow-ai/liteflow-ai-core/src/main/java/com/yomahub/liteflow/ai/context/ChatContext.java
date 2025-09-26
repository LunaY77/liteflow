package com.yomahub.liteflow.ai.context;

import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.slot.DefaultContext;

import java.util.Objects;
import java.util.UUID;

/**
 * chat 上下文, 用于存储 StreamHandler
 * <p>
 * 对于 StreamHandler 参数，可以通过流程参数传入，也可以通过 ChatContext 的构造函数传入
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ChatContext extends DefaultContext {

    private String chatId;

    private StreamHandler streamHandler;

    private ToolRegistry toolRegistry;

    public ChatContext() {
        this(SpringUtil.getBean(StreamHandler.class), SpringUtil.getBean(ToolRegistry.class));
    }

    public ChatContext(ToolRegistry toolRegistry) {
        this(SpringUtil.getBean(StreamHandler.class), toolRegistry);
    }

    public ChatContext(StreamHandler streamHandler) {
        this(streamHandler, SpringUtil.getBean(ToolRegistry.class));
    }

    public ChatContext(StreamHandler streamHandler, ToolRegistry toolRegistry) {
        this.chatId = "chat_" + UUID.randomUUID();
        this.streamHandler = streamHandler;
        this.toolRegistry = toolRegistry;
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

    public ToolRegistry getToolRegistry() {
        return toolRegistry;
    }

    public void setToolRegistry(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private StreamHandler streamHandler;
        private ToolRegistry toolRegistry;

        public Builder streamHandler(StreamHandler streamHandler) {
            this.streamHandler = streamHandler;
            return this;
        }

        public Builder toolRegistry(ToolRegistry toolRegistry) {
            this.toolRegistry = toolRegistry;
            return this;
        }

        public ChatContext build() {
            if (Objects.isNull(toolRegistry)) {
                return new ChatContext(streamHandler);
            } else {
                return new ChatContext(streamHandler, toolRegistry);
            }
        }
    }
}
