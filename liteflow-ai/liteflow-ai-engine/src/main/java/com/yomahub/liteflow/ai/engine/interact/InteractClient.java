package com.yomahub.liteflow.ai.engine.interact;

import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import io.reactivex.rxjava3.core.Flowable;

import java.util.concurrent.CompletableFuture;

/**
 * 交互客户端接口
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface InteractClient {

    Flowable<ChunkEvent> stream(ChatConfig config, ChatRequest request);

    ChatResponse chat(ChatConfig config, ChatRequest request);

    CompletableFuture<ChatResponse> chatAsync(ChatConfig config, ChatRequest request);
}
