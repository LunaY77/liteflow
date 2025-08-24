package com.yomahub.liteflow.ai.model.dashscope.interact;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.interact.protocol.StreamingProtocolType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.engine.util.ObjectMapperHolder;
import com.yomahub.liteflow.ai.model.dashscope.constants.DashScopeConstant;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * DashScope 协议转换器
 *
 * @author 苍镜月
 * @since TODO
 */

public class DashScopeProtocolTransformer implements ProtocolTransformer {

    @Override
    public StreamingProtocolChunk transformStreamingChunk(String streamChunk, InteractContext context) {
        StreamingProtocolChunk protocolChunk = new StreamingProtocolChunk();
        protocolChunk.setId(context.getChatId());

        JsonNode chunkJson = ObjectMapperHolder.readTree(streamChunk);
        JsonNode output = chunkJson.path("output");
        if (output.isMissingNode() || output.isEmpty()) {
            throw new LiteFlowAIEngineException("Invalid DashScope response: 'output' field is missing.");
        }

        JsonNode choices = output.path("choices");
        if (choices.isMissingNode() || choices.isEmpty()) {
            protocolChunk.setType(StreamingProtocolType.TEXT);
            protocolChunk.setData("");
            return protocolChunk;
        }

        JsonNode firstChoice = choices.get(0);
        JsonNode message = firstChoice.path("message");

        // 解析 ToolCall (增量)
        if (message.has("tool_calls")) {
            parseStreamingToolCall(message, context);
            protocolChunk.setType(StreamingProtocolType.TOOL_CALLS);
            protocolChunk.setData(context.getToolCalls());
        }

        // 解析 content
        String content = message.path("content").asText(null);
        if (Objects.nonNull(content)) {
            if (content.contains("<think>")) {
                context.setThinkingInContent(true);
            }

            String processedContent = content.replaceAll("</?think>", "");
            protocolChunk.setData(processedContent);
            protocolChunk.setType(context.isThinkingInContent() ? StreamingProtocolType.THINKING : StreamingProtocolType.TEXT);

            if (content.contains("</think>")) {
                context.setThinkingInContent(false);
            }
        } else {
            // content 为 null，可能使用了思考模式，输出内容在 reasoning_content 字段
            String reasoningContent = output.path("reasoning_content").asText(null);
            if (Objects.nonNull(reasoningContent)) {
                protocolChunk.setType(StreamingProtocolType.THINKING);
                protocolChunk.setData(reasoningContent.replaceAll("</?think>", ""));
            } else {
                // 都不符合，则发送空文本块
                protocolChunk.setType(StreamingProtocolType.TEXT);
                protocolChunk.setData("");
            }
        }

        // 检查流式响应是否结束
        String finishReason = firstChoice.path("finish_reason").asText();
        if (StrUtil.isNotBlank(finishReason) && !finishReason.equalsIgnoreCase("null")) {
            // 这是最后一个数据块, 包含 TokenUsage
            TokenUsage tokenUsage = extractTokenUsage(chunkJson);
            context.setTokenUsage(tokenUsage);
            // 发送 STOP 信号
            protocolChunk.setType(StreamingProtocolType.STOP);
            protocolChunk.setData(finishReason);
        }

        return protocolChunk;
    }

    @Override
    public ChatResponse transformStreamingResponse(InteractContext context) {
        FinishReason finishReason = context.hasToolCalls() ? FinishReason.TOOL_CALL : FinishReason.STOP;

        StringBuilder answer = new StringBuilder();
        if (StrUtil.isNotBlank(context.getAggregatedThinking())) {
            answer.append("<think>").append("\n");
            answer.append(context.getAggregatedThinking());
            answer.append("</think>").append("\n");
        }
        answer.append(context.getAggregatedText());

        AssistantMessage assistantMessage = new AssistantMessage(answer.toString(), context.getToolCalls());
        return new ChatResponse(assistantMessage, context.getTokenUsage(), finishReason);
    }

    @Override
    public ChatResponse transformBlockingResponse(String blockingResponse, InteractContext context) {
        JsonNode responseJson = ObjectMapperHolder.readTree(blockingResponse);
        JsonNode output = responseJson.path("output");
        if (output.isMissingNode() || output.isEmpty()) {
            throw new LiteFlowAIEngineException("Invalid DashScope response: 'output' field is missing.");
        }

        JsonNode choices = output.path("choices");
        if (choices.isMissingNode() || choices.isEmpty()) {
            throw new LiteFlowAIEngineException("Invalid DashScope response: 'choices' field is missing.");
        }

        JsonNode firstChoice = choices.get(0);
        JsonNode message = firstChoice.path("message");

        // 解析 ToolCall (全量)
        List<ToolCall> toolCalls = extractToolCallsFromMessage(message);

        // 解析 FinishReason
        String reasonStr = firstChoice.path("finish_reason").asText();
        FinishReason finishReason = mapFinishReason(reasonStr, toolCalls);

        // 解析 AI 消息内容
        String content = message.path("content").asText(null);
        AssistantMessage assistantMessage = new AssistantMessage(content, toolCalls);

        // 解析 Token 使用情况
        TokenUsage tokenUsage = extractTokenUsage(responseJson);

        return new ChatResponse(assistantMessage, tokenUsage, finishReason);
    }

    /**
     * DashScope 的流式工具调用需要进行增量解析
     *
     * @param messageJson message 节点
     * @param context     交互上下文
     */
    private void parseStreamingToolCall(JsonNode messageJson, InteractContext context) {
        JsonNode toolCallChunks = messageJson.get("tool_calls");
        toolCallChunks.forEach(toolCallChunk -> {
            JsonNode functionJson = toolCallChunk.get("function");

            // 如果 name 不为空，那么认为这是一次新的工具调用
            if (functionJson.has("name") && StrUtil.isNotBlank(functionJson.get("name").asText())) {
                context.addToolCall(
                        ToolCall.builder()
                                .id(toolCallChunk.path("id").asText())
                                .type(toolCallChunk.path("type").asText())
                                .name(functionJson.path("name").asText())
                                .arguments(functionJson.path("arguments").asText(""))
                                .build()
                );
            } else if (functionJson.has("arguments")) {
                // 增量解析后续的 arguments
                Optional.ofNullable(functionJson.get("arguments"))
                        .map(JsonNode::asText)
                        .ifPresent(context::addToolCallArguments);
            }
        });
    }

    private List<ToolCall> extractToolCallsFromMessage(JsonNode messageJson) {
        if (!messageJson.has("tool_calls")) {
            return Collections.emptyList();
        }

        JsonNode toolCalls = messageJson.get("tool_calls");
        if (CollectionUtil.isEmpty(toolCalls)) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(toolCalls.spliterator(), false)
                .map(toolCallJson -> {
                    JsonNode functionJson = toolCallJson.get("function");
                    if (Objects.nonNull(functionJson) && !functionJson.isNull()) {
                        return ToolCall.builder()
                                .id(toolCallJson.path("id").asText())
                                .type(toolCallJson.path("type").asText("function"))
                                .name(functionJson.path("name").asText())
                                .arguments(functionJson.path("arguments").asText())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private TokenUsage extractTokenUsage(JsonNode responseJson) {
        JsonNode usageJson = responseJson.path("usage");
        if (usageJson.isMissingNode() || usageJson.isNull()) {
            return null;
        }

        Integer inputTokens = usageJson.path("input_tokens").asInt();
        Integer outputTokens = usageJson.path("output_tokens").asInt();
        Integer totalTokens = usageJson.path("total_tokens").asInt();
        return new TokenUsage(inputTokens, outputTokens, totalTokens);
    }

    private FinishReason mapFinishReason(String reason, List<ToolCall> toolCalls) {
        if ("tool_calls".equals(reason) || CollectionUtil.isNotEmpty(toolCalls)) {
            return FinishReason.TOOL_CALL;
        }
        if ("stop".equals(reason)) {
            return FinishReason.STOP;
        }
        if ("length".equals(reason)) {
            return FinishReason.LENGTH;
        }
        return FinishReason.OTHER;
    }

    @Override
    public String getProviderName() {
        return DashScopeConstant.PROVIDER_NAME;
    }
}
