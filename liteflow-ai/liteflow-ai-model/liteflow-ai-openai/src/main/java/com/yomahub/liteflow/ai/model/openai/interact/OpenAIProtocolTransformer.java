package com.yomahub.liteflow.ai.model.openai.interact;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.AbstractProtocolTransformer;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.model.openai.constants.OpenAIConstant;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * OpenAI 协议转换器
 *
 * @author 苍镜月
 * @since TODO
 */

public class OpenAIProtocolTransformer extends AbstractProtocolTransformer {

    /**
     * OpenAI 的流式响应的 ToolCall 属于初始定义 + 增量解析
     *
     * @param responseJson 完整的响应 JSON 对象
     * @param context      交互上下文
     * @see <a href="https://platform.openai.com/docs/guides/function-calling#streaming">OpenAI Function Calling</a>
     */
    @Override
    protected void parseStreamingToolCall(JsonNode responseJson, InteractContext context) {
        JsonNode message = extractMessage(responseJson);
        if (message == null || !message.has("tool_calls")) {
            return;
        }

        JsonNode toolCallChunks = message.get("tool_calls");

        toolCallChunks.forEach(toolCallChunk -> {
            JsonNode functionJson = toolCallChunk.get("function");

            // OpenAI 的流式工具调用需要进行增量解析
            // 如果 name 不为空，那么认为这是一次新的工具调用
            if (functionJson.has("name") && StrUtil.isNotBlank(functionJson.get("name").asText())) {
                // 添加新的工具调用到上下文
                context.addToolCall(
                        ToolCall.builder()
                                .id(toolCallChunk.path("id").asText())
                                .type(toolCallChunk.path("type").asText())
                                .name(functionJson.path("name").asText())
                                .arguments(functionJson.path("arguments").asText())
                                .build()
                );
            } else {
                // 增量解析后续的 arguments
                Optional.ofNullable(functionJson.get("arguments"))
                        .map(JsonNode::asText)
                        .ifPresent(context::addToolCallArguments);
            }
        });
    }

    @Override
    protected List<ToolCall> extractToolCalls(JsonNode responseJson) {
        JsonNode message = extractMessage(responseJson);
        if (message == null || !message.has("tool_calls")) {
            return Collections.emptyList();
        }

        JsonNode toolCalls = message.get("tool_calls");
        if (toolCalls == null || toolCalls.isEmpty()) {
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

    @Override
    protected JsonNode extractMessage(JsonNode responseJson) {
        JsonNode choices = extractChoices(responseJson);
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        JsonNode firstChoice = choices.get(0);
        JsonNode message = firstChoice.path("message");
        if (message.isMissingNode() || message.isNull()) {
            return firstChoice.path("delta");
        }
        return message;
    }

    @Override
    protected String extractContent(JsonNode messageJson) {
        if (Objects.isNull(messageJson) || messageJson.isNull()) return null;
        String content = messageJson.path("content").asText(null);
        if (StrUtil.isBlank(content)) {
            content = messageJson.path("refusal").asText(null);
        }
        if (StrUtil.isBlank(content)) {
            content = messageJson.path("reasoning_content").asText(null);
        }
        return content;
    }

    @Override
    protected String extractThinkingContent(JsonNode messageJson) {
        String content = extractContent(messageJson);
        if (content == null) return "";
        return content.replaceAll("</?think>", "");
    }

    @Override
    protected boolean isResponseDone(JsonNode responseJson) {
        JsonNode choices = extractChoices(responseJson);
        if (choices == null || choices.isEmpty()) return false;
        String finishReason = choices.get(0).path("finish_reason").asText();
        return StrUtil.isNotBlank(finishReason);
    }

    @Override
    protected TokenUsage extractTokenUsage(JsonNode responseJson) {
        JsonNode usageJson = responseJson.path("usage");
        if (usageJson.isMissingNode() || usageJson.isNull()) {
            return null;
        }

        Integer promptTokens = usageJson.path("prompt_tokens").asInt();
        Integer completionTokens = usageJson.path("completion_tokens").asInt();
        Integer totalTokens = usageJson.path("total_tokens").asInt();
        return new TokenUsage(promptTokens, completionTokens, totalTokens);
    }

    @Override
    protected boolean isThinkingStart(JsonNode messageJson) {
        if (Objects.isNull(messageJson) || messageJson.isNull()) return false;
        return Optional.ofNullable(extractContent(messageJson))
                .map(content -> content.contains("<think>"))
                .orElse(false);
    }

    @Override
    protected boolean isThinkingEnd(JsonNode messageJson) {
        if (Objects.isNull(messageJson) || messageJson.isNull()) return false;
        return Optional.ofNullable(extractContent(messageJson))
                .map(content -> content.contains("</think>"))
                .orElse(false);
    }

    @Override
    public String getProviderName() {
        return OpenAIConstant.PROVIDER_NAME;
    }

    private JsonNode extractChoices(JsonNode responseJson) {
        return responseJson.path("choices");
    }
}
