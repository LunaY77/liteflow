package com.yomahub.liteflow.ai.interact.protocol;

import com.yomahub.liteflow.ai.interact.pipeline.ChatContext;
import com.yomahub.liteflow.ai.interact.pipeline.ChunkTransformer;
import com.yomahub.liteflow.ai.model.chat.message.AssistantMessage;
import okhttp3.Response;

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
     * @param chunk 流式响应的 chunk
     * @param context  上下文信息，包含会话状态等
     * @return 转换后的 chunk
     */
    StreamingProtocolChunk transform(String chunk, ChatContext context);

    /**
     * 将 阻塞式调用 的 Response 转换为 LiteFlow-AI 支持的 AssistantMessage。
     *
     * @param response OkHttp 的 Response 对象
     * @param context 上下文信息，包含会话状态等
     * @return 转换后的 AssistantMessage
     */
    AssistantMessage transform(Response response, ChatContext context);

    @Override
    default String getTransformerType() {
        return "ProtocolTransformer";
    }
}
