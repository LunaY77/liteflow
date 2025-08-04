package com.yomahub.liteflow.ai.engine.interact.protocol;

import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkTransformer;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

/**
 * 协议转换器
 * 将不同厂商大模型响应转换为 LiteFlow-AI 支持的统一格式。
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ProtocolTransformer extends ChunkTransformer {

    /**
     * 将流式响应的 chunk 转换为 LiteFlow-AI 支持的格式。
     *
     * @param streamChunk 流式响应的 chunk
     * @param context     上下文信息，包含会话状态等
     * @return 转换后的 chunk
     */
    StreamingProtocolChunk transformStreamingChunk(String streamChunk, InteractContext context);

    /**
     * 从聊天上下文构造最终的 ChatResponse（用于流式调用）
     *
     * @param context 聊天上下文，包含累积的文本内容等信息
     * @return 构造的最终 ChatResponse
     */
    ChatResponse transformStreamingResponse(InteractContext context);

    /**
     * 将 阻塞式调用 的 Response 转换为 LiteFlow-AI 支持的 ChatResponse
     *
     * @param blockingResponse Response Body JSON String
     * @param context          上下文信息，包含会话状态等
     * @return 转换后的 ChatResponse
     */
    ChatResponse transformBlockingResponse(String blockingResponse, InteractContext context);

    String getProviderName();

    @Override
    default String getTransformerType() {
        return "ProtocolTransformer";
    }
}
