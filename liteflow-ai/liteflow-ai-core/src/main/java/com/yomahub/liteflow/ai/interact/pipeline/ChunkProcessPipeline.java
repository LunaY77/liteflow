package com.yomahub.liteflow.ai.interact.pipeline;

import com.yomahub.liteflow.ai.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.interact.protocol.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.model.chat.message.AssistantMessage;
import okhttp3.Response;

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

    private ChunkProcessPipeline(ChatContext context, ProtocolTransformer protocolTransformer, ChunkCallbackTransformer chunkCallbackTransformer) {
        this.context = context;
        this.protocolTransformer = protocolTransformer;
        this.chunkCallbackTransformer = chunkCallbackTransformer;
    }

    /**
     * 创建流式消息处理管道实例。
     *
     * @param context             聊天上下文，包含会话状态等
     * @param protocolTransformer 协议转换器，将不同厂商大模型响应转换为 LiteFlow-AI 支持的统一格式
     * @param chunkCallbackTransformer       消息处理管道的回调接口，根据块数据的类型进行具体回调
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
    public StreamingProtocolChunk process(String chunk) {
        // 转换 chunk
        StreamingProtocolChunk transformedChunk = protocolTransformer.transform(chunk, context);

        // 更新上下文
        switch (transformedChunk.getType()) {
            case TEXT:
                String textContent = (String) transformedChunk.getData();
                String callbackText = chunkCallbackTransformer.onText(textContent, context);
                transformedChunk.setData(callbackText);
                context.addText(callbackText);
                break;
            case THINKING:
                String thinkingContent = (String) transformedChunk.getData();
                String callbackThinking = chunkCallbackTransformer.onThinking(thinkingContent, context);
                transformedChunk.setData(callbackThinking);
                context.addThinking(callbackThinking);
                break;
            case TOOL_CALLS:
                Object toolCallsContent = transformedChunk.getData();
                Object responseToolCalls = chunkCallbackTransformer.onToolsCalling(toolCallsContent, context);
                transformedChunk.setData(responseToolCalls);
                break;
            case USAGE:
                Object usageContent = transformedChunk.getData();
                Object responseUsage = chunkCallbackTransformer.onUsage(usageContent, context);
                transformedChunk.setData(responseUsage);
                break;
            case BASE64_IMAGE:
            case DATA:
            case ERROR:
                // TODO 异常处理
            default:
                // 其他类型暂不处理
                break;
        }

        return transformedChunk;
    }

    /**
     * 将 阻塞式调用 的 Response 转换为 LiteFlow-AI 支持的 AssistantMessage。
     *
     * @param response OkHttp 的 Response 对象
     * @return 转换后的 AssistantMessage
     */
    public AssistantMessage process(Response response) {
        return protocolTransformer.transform(response, context);
    }
}
