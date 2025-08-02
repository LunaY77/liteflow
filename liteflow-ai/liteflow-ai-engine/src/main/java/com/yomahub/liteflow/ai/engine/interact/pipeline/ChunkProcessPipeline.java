package com.yomahub.liteflow.ai.engine.interact.pipeline;

import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

/**
 * 流式消息处理管道
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChunkProcessPipeline {

    private final ChatContext context;

    private final ProtocolTransformer protocolTransformer;

    private final ChunkCallbackTransformer chunkCallbackTransformer;

    private ChunkProcessPipeline(
            ChatContext context,
            ProtocolTransformer protocolTransformer,
            ChunkCallbackTransformer chunkCallbackTransformer) {
        this.context = context;
        this.protocolTransformer = protocolTransformer;
        this.chunkCallbackTransformer = chunkCallbackTransformer;
    }

    /**
     * 创建流式消息处理管道实例。
     *
     * @param context                  聊天上下文，包含会话状态等
     * @param protocolTransformer      协议转换器，将不同厂商大模型响应转换为 LiteFlow-AI 支持的统一格式
     * @param chunkCallbackTransformer 消息处理管道的回调接口，根据块数据的类型进行具体回调
     * @return 流式消息处理管道实例
     */
    public static ChunkProcessPipeline createStreamingPipeline(ChatContext context, ProtocolTransformer protocolTransformer, ChunkCallbackTransformer chunkCallbackTransformer) {
        return new ChunkProcessPipeline(context, protocolTransformer, chunkCallbackTransformer);
    }

    /**
     * 创建阻塞式调用的消息处理管道实例。
     *
     * @param context             聊天上下文，包含会话状态等
     * @param protocolTransformer 协议转换器，将不同厂商大模型响应转换为 LiteFlow-AI 支持的统一格式
     * @return 阻塞式调用的消息处理管道实例
     */
    public static ChunkProcessPipeline createBlockingPipeline(ChatContext context, ProtocolTransformer protocolTransformer) {
        return new ChunkProcessPipeline(context, protocolTransformer, null);
    }

    /**
     * 将流式响应的 chunk 转换为 LiteFlow-AI 支持的格式。
     *
     * @param chunk 流式响应的 chunk
     * @return 转换后的 chunk
     */
    public StreamingProtocolChunk processStreaming(String chunk) {
        // 协议转换 chunk
        StreamingProtocolChunk transformedChunk = protocolTransformer.transformStreamingChunk(chunk, context);

        transformedChunk = chunkCallbackTransformer.transform(transformedChunk, context);

        return transformedChunk;
    }

    /**
     * 将 阻塞式调用 的 Response 转换为 LiteFlow-AI 支持的 AssistantMessage。
     *
     * @param blockingResponse OkHttp 的 Response 对象
     * @return 转换后的 ChatResponse
     */
    public ChatResponse processBlocking(String blockingResponse) {
        return protocolTransformer.transformBlockingResponse(blockingResponse, context);
    }

    /**
     * 构造流式调用的最终响应
     *
     * @return 最终的 ChatResponse
     */
    public ChatResponse buildFinalStreamingResponse() {
        return protocolTransformer.transformStreamingResponse(context);
    }

    public ChatContext getContext() {
        return context;
    }
}
