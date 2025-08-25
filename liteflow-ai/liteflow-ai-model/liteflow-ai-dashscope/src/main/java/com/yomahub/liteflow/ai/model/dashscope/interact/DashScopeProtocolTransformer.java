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
 * DashScope 协议转换器(和 OpenAI 基本一致)
 *
 * @author 苍镜月
 * @since TODO
 */

public class DashScopeProtocolTransformer implements ProtocolTransformer {

    @Override
    public StreamingProtocolChunk transformStreamingChunk(String streamChunk, InteractContext context) {
        StreamingProtocolChunk protocolChunk = new StreamingProtocolChunk();
        protocolChunk.setId(context.getChatId());

        // OpenAI 流结束的标志
        if ("[DONE]".equals(streamChunk.trim())) {
            protocolChunk.setType(StreamingProtocolType.STOP);
            protocolChunk.setData("[DONE]");
            context.setFinished(true);
            return protocolChunk;
        }

        JsonNode chunkJson = ObjectMapperHolder.readTree(streamChunk);
        JsonNode choices = chunkJson.path("choices");

        if (choices.isMissingNode() || choices.isEmpty()) {
            // 检查流式响应最后的 usage
            if (chunkJson.has("usage") && !chunkJson.get("usage").isNull()) {
                context.setTokenUsage(extractTokenUsage(chunkJson));
                protocolChunk.setType(StreamingProtocolType.USAGE);
                protocolChunk.setData(context.getTokenUsage());
            } else {
                protocolChunk.setType(StreamingProtocolType.TEXT);
                protocolChunk.setData("");
            }
            return protocolChunk;
        }

        JsonNode firstChoice = choices.get(0);
        JsonNode delta = firstChoice.path("delta");

        // 解析 ToolCall (增量)
        if (delta.has("tool_calls")) {
            parseStreamingToolCall(delta, context);
            protocolChunk.setType(StreamingProtocolType.TOOL_CALLS);
            protocolChunk.setData(context.getToolCalls());
            return protocolChunk;
        }

        // 解析 content
        String content = extractContentFromDelta(delta);
        if (Objects.nonNull(content)) {
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
            // content 为 null，可能使用了思考模式，输出内容在 reasoning_content 字段
            String reasoningContent = delta.path("reasoning_content").asText(null);
            if (Objects.nonNull(reasoningContent)) {
                protocolChunk.setType(StreamingProtocolType.THINKING);
                protocolChunk.setData(reasoningContent.replaceAll("</?think>", ""));
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
        JsonNode choices = responseJson.path("choices");
        if (choices.isMissingNode() || choices.isEmpty()) {
            throw new LiteFlowAIEngineException("Invalid OpenAI response: 'choices' field is missing or empty.");
        }
        JsonNode firstChoice = choices.get(0);
        JsonNode message = firstChoice.path("message");

        // 解析 ToolCall (全量)
        List<ToolCall> toolCalls = extractToolCallsFromMessage(message);

        // 解析 FinishReason
        String reasonStr = firstChoice.path("finish_reason").asText();
        FinishReason finishReason = mapFinishReason(reasonStr, toolCalls);

        // 解析 AI 消息内容
        String content = extractContentFromMessage(message);
        AssistantMessage assistantMessage = new AssistantMessage(content, toolCalls);

        // 解析 Token 使用情况
        TokenUsage tokenUsage = extractTokenUsage(responseJson);

        return new ChatResponse(assistantMessage, tokenUsage, finishReason);
    }

    /**
     * OpenAI 的流式响应的 ToolCall 属于初始定义 + 增量解析
     *
     * @param deltaJson choices中的delta节点
     * @param context   交互上下文
     * @see <a href="https://platform.openai.com/docs/guides/function-calling#streaming">OpenAI Function Calling</a>
     */
    private void parseStreamingToolCall(JsonNode deltaJson, InteractContext context) {
        JsonNode toolCallChunks = deltaJson.get("tool_calls");
        toolCallChunks.forEach(toolCallChunk -> {
            JsonNode functionJson = toolCallChunk.get("function");
            // 如果 name 不为空，那么认为这是一次新的工具调用
            if (functionJson.has("name") && StrUtil.isNotBlank(functionJson.get("name").asText())) {
                context.addToolCall(
                        ToolCall.builder()
                                .id(toolCallChunk.path("id").asText())
                                .type(toolCallChunk.path("type").asText())
                                .name(functionJson.path("name").asText())
                                .arguments(functionJson.path("arguments").asText("")) // arguments可能为空
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
        if (Objects.isNull(messageJson) || !messageJson.has("tool_calls")) {
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
                                .type(toolCallJson.path("type").asText())
                                .name(functionJson.path("name").asText())
                                .arguments(functionJson.path("arguments").asText())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String extractContentFromMessage(JsonNode messageJson) {
        if (Objects.isNull(messageJson) || !messageJson.has("content")) return null;
        return messageJson.path("content").asText(null);
    }

    private String extractContentFromDelta(JsonNode deltaJson) {
        if (Objects.isNull(deltaJson) || !deltaJson.has("content")) return null;
        return deltaJson.path("content").asText(null);
    }

    private TokenUsage extractTokenUsage(JsonNode responseJson) {
        JsonNode usageJson = responseJson.path("usage");
        if (usageJson.isMissingNode() || usageJson.isNull()) {
            return null;
        }

        Integer promptTokens = usageJson.path("prompt_tokens").asInt();
        Integer completionTokens = usageJson.path("completion_tokens").asInt();
        Integer totalTokens = usageJson.path("total_tokens").asInt();
        return new TokenUsage(promptTokens, completionTokens, totalTokens);
    }

    private FinishReason mapFinishReason(String reason, List<ToolCall> toolCalls) {
        if (CollectionUtil.isNotEmpty(toolCalls) && "tool_calls".equals(reason)) {
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
