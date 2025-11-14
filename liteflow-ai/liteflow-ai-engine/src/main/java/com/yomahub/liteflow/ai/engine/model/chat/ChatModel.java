package com.yomahub.liteflow.ai.engine.model.chat;

import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.model.BaseModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import org.reactivestreams.Publisher;

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
     * 执行聊天请求(响应式流式)
     * <p>
     * 返回一个 Flowable 流，用户可以订阅和处理每个 ChunkEvent 事件，包括：
     * - START：流开始事件，包含初始化的 InteractContext
     * - CHUNK：数据分块事件，包含原始 JSON、转换后的分块和更新的 InteractContext
     * - COMPLETE：完成事件，包含最终的 ChatResponse 和完整的 InteractContext
     * - ERROR：错误事件，包含异常信息
     *
     * @param request 聊天请求
     * @return 包含所有流式事件的 Flowable 流
     */
    Publisher<ChunkEvent> stream(ChatRequest request);
}
