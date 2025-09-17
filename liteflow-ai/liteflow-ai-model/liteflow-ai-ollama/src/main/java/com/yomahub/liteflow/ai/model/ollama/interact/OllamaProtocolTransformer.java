package com.yomahub.liteflow.ai.model.ollama.interact;

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
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Ollama 协议转换器
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaProtocolTransformer implements ProtocolTransformer {

    @Override
    public StreamingProtocolChunk transformStreamingChunk(String streamChunk, InteractContext context) {
        StreamingProtocolChunk protocolChunk = new StreamingProtocolChunk();
        protocolChunk.setId(context.getChatId());

        JsonNode chunkJson = ObjectMapperHolder.readTree(streamChunk);

        // 检查流式响应是否结束
        boolean isDone = chunkJson.path("done").asBoolean(false);
        if (isDone) {
            // 最后一个数据块，包含 TokenUsage
            TokenUsage tokenUsage = extractTokenUsage(chunkJson);
            context.setTokenUsage(tokenUsage);
            protocolChunk.setType(StreamingProtocolType.STOP);
            protocolChunk.setData("done");
            return protocolChunk;
        }

        JsonNode message = chunkJson.path("message");
        if (message.isMissingNode() || message.isNull()) {
            protocolChunk.setType(StreamingProtocolType.TEXT);
            protocolChunk.setData("");
            return protocolChunk;
        }

        // 解析 ToolCall (Ollama在流式中一次性返回)
        if (message.has("tool_calls")) {
            List<ToolCall> toolCalls = extractToolCallsFromMessage(message);
            if (CollectionUtil.isNotEmpty(toolCalls)) {
                context.setToolCalls(toolCalls);
                protocolChunk.setType(StreamingProtocolType.TOOL_CALLS);
                protocolChunk.setData(toolCalls);
                return protocolChunk;
            }
        }

        // 解析 content
        String content = message.path("content").asText(null);
        if (StrUtil.isNotBlank(content)) {
            // 判断是否为思考内容
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
            // content 为空，可能使用了思考模式，输出内容在 thinking 字段中
            String thinking = message.path("thinking").asText(null);
            if (StrUtil.isNotBlank(thinking)) {
                protocolChunk.setType(StreamingProtocolType.THINKING);
                protocolChunk.setData(thinking.replaceAll("</?think>", ""));
            } else {
                // 都不符合，则发送空文本块
                protocolChunk.setType(StreamingProtocolType.TEXT);
                protocolChunk.setData("");
            }
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

        boolean isDone = responseJson.path("done").asBoolean(false);
        if (!isDone) {
            throw new LiteFlowAIEngineException("Ollama blocking response is not done yet, please check the response.");
        }

        JsonNode message = responseJson.path("message");
        if (message.isMissingNode() || message.isNull()) {
            throw new LiteFlowAIEngineException("Invalid Ollama response: 'message' field is missing or empty.");
        }

        // 解析 ToolCall (全量)
        List<ToolCall> toolCalls = extractToolCallsFromMessage(message);

        // 解析 FinishReason
        FinishReason finishReason = CollectionUtil.isNotEmpty(toolCalls) ? FinishReason.TOOL_CALL : FinishReason.STOP;

        // 解析 AI 消息内容
        String content = message.path("content").asText("");
        AssistantMessage assistantMessage = new AssistantMessage(content, toolCalls);

        // 解析 Token 使用情况
        TokenUsage tokenUsage = extractTokenUsage(responseJson);

        return new ChatResponse(assistantMessage, tokenUsage, finishReason);
    }

    private List<ToolCall> extractToolCallsFromMessage(JsonNode messageJson) {
        if (!messageJson.has("tool_calls")) {
            return Collections.emptyList();
        }

        JsonNode toolCalls = messageJson.get("tool_calls");
        if (Objects.isNull(toolCalls) || toolCalls.isEmpty()) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(toolCalls.spliterator(), false)
                .map(toolCallJson -> {
                    JsonNode functionJson = toolCallJson.get("function");
                    if (Objects.nonNull(functionJson) && !functionJson.isNull()) {
                        return ToolCall.builder()
                                .type("function")
                                .name(functionJson.path("name").asText())
                                .arguments(functionJson.path("arguments"))
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private TokenUsage extractTokenUsage(JsonNode responseJson) {
        if (responseJson.isMissingNode() || responseJson.isNull()) {
            return null;
        }
        int promptTokens = responseJson.path("prompt_eval_count").asInt(0);
        int completionTokens = responseJson.path("eval_count").asInt(0);
        if (promptTokens == 0 && completionTokens == 0) {
            return null;
        }
        return new TokenUsage(promptTokens, completionTokens);
    }

    @Override
    public String getProviderName() {
        return OllamaConstant.PROVIDER_NAME;
    }
}
