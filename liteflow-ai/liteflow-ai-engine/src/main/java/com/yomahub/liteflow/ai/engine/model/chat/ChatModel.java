package com.yomahub.liteflow.ai.engine.model.chat;

import com.yomahub.liteflow.ai.engine.model.BaseModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

import java.util.concurrent.CompletableFuture;

/**
 * 聊天模型
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface ChatModel extends BaseModel<ChatConfig> {

    /**
     * 执行聊天请求(同步)
     *
     * @param request 聊天请求
     * @return 聊天响应
     */
    ChatResponse chat(ChatRequest request);

    /**
     * 执行聊天请求(异步)
     *
     * @param request 聊天请求
     * @return 异步聊天响应
     */
    CompletableFuture<ChatResponse> chatAsync(ChatRequest request);

    /**
     * 执行聊天请求(异步流式)
     *
     * @param request 聊天请求
     */
    void stream(ChatRequest request);

}
