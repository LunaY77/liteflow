package com.yomahub.liteflow.ai.engine.interact.protocol;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.engine.util.ObjectMapperHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        System.out.println(streamChunk);
        StreamingProtocolChunk protocolChunk = new StreamingProtocolChunk();
        protocolChunk.setId(context.getChatId());

        if ("[DONE]".equals(streamChunk)) {
            protocolChunk.setType(StreamingProtocolType.STOP);
            protocolChunk.setData(streamChunk);
            return protocolChunk;
        }
        JsonNode chunkJson = ObjectMapperHolder.readTree(streamChunk);

        // 尝试获取 Token 使用情况
        Optional.ofNullable(extractTokenUsage(chunkJson))
                .ifPresent(context::setTokenUsage);

        // 获取响应消息
        JsonNode message = extractMessage(chunkJson);

        // 如果 message 为空，表示当前 chunk 没有 AI 响应内容
        // 可能是收尾数据块，比如 TokenUsage
        if (Objects.isNull(message) || message.isNull()) {
            protocolChunk.setType(StreamingProtocolType.TEXT);
            protocolChunk.setData("");
            return protocolChunk;
        }

        String content = extractContent(message);
        boolean isDone = isResponseDone(chunkJson);

        // 流式响应结束
        // 这里的结束代表模型给出了具体的 FinishReason，但是不代表流式传输结束
        // 例如 OpenAI 的流式响应，可能在 FinisReason 之后会给出 TokenUsage 数据块
        if (isDone && StrUtil.isBlank(content)) {
            protocolChunk.setType(StreamingProtocolType.STOP);
            protocolChunk.setData(FINISHED_DATA);
            return protocolChunk;
        }

        // 流式响应未结束

        // 解析 ToolCall
        parseStreamingToolCall(chunkJson, context);

        // 如果上下文中存在工具调用，需要进行解析
        if (context.hasToolCalls()) {
            protocolChunk.setType(StreamingProtocolType.TOOL_CALLS);
            protocolChunk.setData(context.getToolCalls());
            return protocolChunk;
        }

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
        protocolChunk.setData(extractThinkingContent(message));

        return protocolChunk;
    }

    @Override
    public ChatResponse transformStreamingResponse(InteractContext context) {
        // 从上下文中获取 ToolCall 列表
        List<ToolCall> toolCalls = context.getToolCalls();
        FinishReason finishReason = context.hasToolCalls() ?
                FinishReason.TOOL_CALL : FinishReason.STOP;

        StringBuilder answer = new StringBuilder();
        if (StrUtil.isNotBlank(context.getAggregatedThinking())) {
            answer.append("<think>").append("\n");
            answer.append(context.getAggregatedThinking());
            answer.append("</think>").append("\n");
        }
        answer.append(context.getAggregatedText());

        AssistantMessage assistantMessage = new AssistantMessage(answer.toString(), toolCalls);

        return new ChatResponse(assistantMessage, context.getTokenUsage(), finishReason);
    }

    @Override
    public ChatResponse transformBlockingResponse(String blockingResponse, InteractContext context) {
        JsonNode responseJson = ObjectMapperHolder.readTree(blockingResponse);

        boolean isDone = isResponseDone(responseJson);
        if (!isDone) {
            throw new LiteFlowAIEngineException("blocking response is not done yet, please check the response.");
        }

        // 解析 ToolCall
        List<ToolCall> toolCalls = extractToolCalls(responseJson);
        FinishReason finishReason = CollectionUtil.isNotEmpty(toolCalls) ?
                FinishReason.TOOL_CALL : FinishReason.STOP;

        // 解析 AI 消息内容
        JsonNode message = extractMessage(responseJson);
        // 组装 AI Message
        AssistantMessage assistantMessage = new AssistantMessage(extractContent(message), toolCalls);
        // 解析 Token 使用情况
        TokenUsage tokenUsage = extractTokenUsage(responseJson);

        return new ChatResponse(assistantMessage, tokenUsage, finishReason);
    }

    /**
     * 从流式响应中的单个数据块中解析 ToolCall 信息，并将其添加到上下文中。
     * <p>
     * 对于流式响应的 ToolCall，可能存在两种情况：
     * <ol>
     * <li>初始定义 + 增量解析：第一个块只包含 id，name，type 等信息。后续的块只包含 arguments 片段</li>
     * <li>全量解析：数据块中包含完整的 ToolCall 信息</li>
     * </ol>
     *
     * @param responseJson 完整的响应 JSON 对象
     * @param context      交互上下文
     */
    protected abstract void parseStreamingToolCall(JsonNode responseJson, InteractContext context);

    /**
     * 从（阻塞式响应）中提取 ToolCall 列表。（虽然这里是 List，但是暂时不支持并行工具调用）
     *
     * @param responseJson 完整的响应 JSON 对象
     * @return ToolCall 列表
     */
    protected abstract List<ToolCall> extractToolCalls(JsonNode responseJson);

    /**
     * 从响应的 JSON 中提取 AI 的消息内容。
     *
     * @param responseJson 完整的响应 JSON 对象
     * @return AI 消息内容
     */
    protected abstract JsonNode extractMessage(JsonNode responseJson);

    /**
     * 从响应的 JSON 中提取 AI 的消息字符串。
     *
     * @param messageJson Message JSON 对象
     * @return 消息内容字符串
     */
    protected abstract String extractContent(JsonNode messageJson);

    /**
     * 从响应的 JSON 中提取 AI 的思考内容。
     *
     * @param messageJson Message JSON 对象
     * @return 思考内容字符串
     */
    protected abstract String extractThinkingContent(JsonNode messageJson);

    /**
     * 检查响应是否已完成且成功。
     *
     * @param responseJson 完整的响应 JSON 对象
     * @return 如果响应完成且成功，则为 true
     */
    protected abstract boolean isResponseDone(JsonNode responseJson);

    /**
     * 从阻塞式响应的 JSON 中提取 Token 使用情况。
     *
     * @param responseJson 完整的响应 JSON 对象
     * @return TokenUsage 实例
     */
    protected abstract TokenUsage extractTokenUsage(JsonNode responseJson);

    /**
     * 判断当前 chunk 是否为思考开始的标志(流式解析专用)
     *
     * @param messageJson Message JSON 对象
     * @return 如果当前 chunk 表示思考开始，则为 true
     */
    protected abstract boolean isThinkingStart(JsonNode messageJson);

    /**
     * 判断当前 chunk 是否为思考结束的标志(流式解析专用)
     *
     * @param messageJson Message JSON 对象
     * @return 如果当前 chunk 表示思考结束，则为 true
     */
    protected abstract boolean isThinkingEnd(JsonNode messageJson);
}
