package com.yomahub.liteflow.ai.model.chat;

import com.yomahub.liteflow.ai.model.BaseModel;
import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.model.chat.message.Message;

import java.util.List;

/**
 * 聊天模型
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ChatModel extends BaseModel<ChatConfig> {

    ChatResponse call(ChatRequest request);

    default ChatResponse call(List<Message> messages) {
        return call(new ChatRequest(messages, ChatOptions.DEFAULT));
    }

//    void stream(ChatRequest request, StreamHandler streamHandler);
}
