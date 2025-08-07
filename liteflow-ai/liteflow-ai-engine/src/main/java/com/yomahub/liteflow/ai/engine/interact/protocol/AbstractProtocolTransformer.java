package com.yomahub.liteflow.ai.engine.interact.protocol;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;

import java.util.Objects;

/**
 * 抽象协议转换器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractProtocolTransformer implements ProtocolTransformer {

    private static final String FINISHED_DATA = "finished";

    @Override
    public StreamingProtocolChunk transformStreamingChunk(String streamChunk, InteractContext context) {
        JSONObject chunkJson = JSONObject.parseObject(streamChunk);
        StreamingProtocolChunk protocolChunk = new StreamingProtocolChunk();
        protocolChunk.setId(context.getChatId());

        // 获取响应消息
        JSONObject message = extractMessage(chunkJson);
        String content = extractContent(message);
        boolean isDone = isResponseDone(chunkJson);

        // 流式响应结束
        if (isDone) {
            // 获取 Token 使用情况
            TokenUsage tokenUsage = extractTokenUsage(chunkJson);
            context.setTokenUsage(tokenUsage);

            // 转换为流式协议
            if (Objects.isNull(message) || (StrUtil.isBlank(content))) {
                protocolChunk.setType(StreamingProtocolType.STOP);
                protocolChunk.setData(FINISHED_DATA);
                return protocolChunk;
            }
        }

        // 流式响应未结束

        // TODO 解析 ToolCall

        // 判断是否为思考开始或结束
        if (isThinkingStart(message)) {
            // 思考开始, 保存状态
            context.setThinkingInContent(true);
        }
        if (isThinkingEnd(message)) {
            // 思考结束，清除状态
            context.setThinkingInContent(false);
        }

        // 设置 message
        protocolChunk.setType(context.isThinkingInContent() ?
                StreamingProtocolType.THINKING : StreamingProtocolType.TEXT);
        protocolChunk.setData(context.isThinkingInContent() ?
                extractThinkingContent(message) : content);

        return protocolChunk;
    }

    @Override
    public ChatResponse transformStreamingResponse(InteractContext context) {
        // TODO 解析 ToolCall

        StringBuilder stringBuilder = new StringBuilder();
        if (StrUtil.isNotBlank(context.getAggregatedThinking())) {
            stringBuilder.append("<think>");
            stringBuilder.append(context.getAggregatedThinking());
            stringBuilder.append("</think>");
            stringBuilder.append("\n");
        }
        stringBuilder.append(context.getAggregatedText());

        AssistantMessage assistantMessage = new AssistantMessage(stringBuilder.toString());

        return new ChatResponse(assistantMessage, context.getTokenUsage(), FinishReason.STOP);
    }

    @Override
    public ChatResponse transformBlockingResponse(String blockingResponse, InteractContext context) {
        JSONObject responseJson = JSONObject.parseObject(blockingResponse);

        boolean isDone = isResponseDone(responseJson);
        if (!isDone) {
            throw new LiteFlowAIEngineException("blocking response is not done yet, please check the response.");
        }

        // TODO 解析 ToolCall

        // 解析 AI 消息内容
        JSONObject message = extractMessage(responseJson);
        // 组装 AI Message
        AssistantMessage assistantMessage = new AssistantMessage(extractContent(message));

        // 解析 Token 使用情况
        TokenUsage tokenUsage = extractTokenUsage(responseJson);

        return new ChatResponse(assistantMessage, tokenUsage, FinishReason.STOP);
    }

    /**
     * 从响应的 JSON 中提取 AI 的消息内容。
     *
     * @param responseJson 完整的响应 JSON 对象
     * @return AI 消息内容
     */
    protected abstract JSONObject extractMessage(JSONObject responseJson);

    /**
     * 从响应的 JSON 中提取 AI 的消息字符串。
     *
     * @param messageJson Message JSON 对象
     * @return 消息内容字符串
     */
    protected abstract String extractContent(JSONObject messageJson);

    /**
     * 从响应的 JSON 中提取 AI 的思考内容。
     *
     * @param messageJson Message JSON 对象
     * @return 思考内容字符串
     */
    protected abstract String extractThinkingContent(JSONObject messageJson);

    /**
     * 检查响应是否已完成且成功。
     *
     * @param responseJson 完整的响应 JSON 对象
     * @return 如果响应完成且成功，则为 true
     */
    protected abstract boolean isResponseDone(JSONObject responseJson);

    /**
     * 从阻塞式响应的 JSON 中提取 Token 使用情况。
     *
     * @param responseJson 完整的响应 JSON 对象
     * @return TokenUsage 实例
     */
    protected abstract TokenUsage extractTokenUsage(JSONObject responseJson);

    /**
     * 判断当前 chunk 是否为思考开始的标志(流式解析专用)
     *
     * @param messageJson Message JSON 对象
     * @return 如果当前 chunk 表示思考开始，则为 true
     */
    protected abstract boolean isThinkingStart(JSONObject messageJson);

    /**
     * 判断当前 chunk 是否为思考结束的标志(流式解析专用)
     *
     * @param messageJson Message JSON 对象
     * @return 如果当前 chunk 表示思考结束，则为 true
     */
    protected abstract boolean isThinkingEnd(JSONObject messageJson);
}
