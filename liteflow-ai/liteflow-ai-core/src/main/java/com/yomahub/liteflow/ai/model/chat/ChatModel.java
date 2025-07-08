package com.yomahub.liteflow.ai.model.chat;

import com.yomahub.liteflow.ai.model.BaseModel;
import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.model.chat.stream.StreamHandler;

/**
 * 聊天模型
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ChatModel extends BaseModel<ChatConfig> {

    ChatResponse call(ChatRequest request);

    void stream(ChatRequest request, StreamHandler streamHandler);
}
