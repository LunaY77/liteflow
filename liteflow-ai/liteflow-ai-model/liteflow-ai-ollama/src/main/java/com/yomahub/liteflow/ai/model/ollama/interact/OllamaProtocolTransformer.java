package com.yomahub.liteflow.ai.model.ollama.interact;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.AbstractProtocolTransformer;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
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

public class OllamaProtocolTransformer extends AbstractProtocolTransformer {

    @Override
    protected void parseStreamingToolCall(JsonNode responseJson, InteractContext context) {
        // ollama 的工具调用信息在 message 中
        JsonNode message = extractMessage(responseJson);
        if (Objects.isNull(message) || message.isNull()) return;
        List<ToolCall> toolCalls = extractToolCallsFromMessage(message);
        if (CollectionUtil.isNotEmpty(toolCalls)) {
            context.setToolCalls(toolCalls);
        }
    }

    @Override
    protected List<ToolCall> extractToolCalls(JsonNode responseJson) {
        JsonNode message = extractMessage(responseJson);
        if (Objects.isNull(message) || message.isNull()) return Collections.emptyList();
        return extractToolCallsFromMessage(message);
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
                                .arguments(functionJson.path("arguments").toString())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    protected JsonNode extractMessage(JsonNode responseJson) {
        return responseJson.path("message");
    }

    @Override
    protected String extractContent(JsonNode messageJson) {
        return messageJson.path("content").asText("");
    }

    @Override
    protected String extractThinkingContent(JsonNode messageJson) {
        return extractContent(messageJson).replaceAll("</?think>", "");
    }

    @Override
    protected boolean isResponseDone(JsonNode responseJson) {
        return responseJson.path("done").asBoolean(false);
    }

    @Override
    protected TokenUsage extractTokenUsage(JsonNode responseJson) {
        Integer promptTokens = responseJson.path("prompt_eval_count").asInt();
        Integer completionTokens = responseJson.path("eval_count").asInt();
        // 计算 Token 使用情况
        return new TokenUsage(promptTokens, completionTokens);
    }

    @Override
    protected boolean isThinkingStart(JsonNode messageJson) {
        return extractContent(messageJson).contains("<think>");
    }

    @Override
    protected boolean isThinkingEnd(JsonNode messageJson) {
        return extractContent(messageJson).contains("</think>");
    }

    @Override
    public String getProviderName() {
        return OllamaConstant.PROVIDER_NAME;
    }
}
