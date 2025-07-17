package com.yomahub.liteflow.ai.interact;

import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;

import java.util.concurrent.CompletableFuture;

/**
 * 交互客户端接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface InteractClient {

    void stream(ChatConfig config, ChatRequest request);

    ChatResponse chat(ChatConfig config, ChatRequest request);

    CompletableFuture<ChatResponse> chatAsync(ChatConfig config, ChatRequest request);
}
