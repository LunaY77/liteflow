package com.yomahub.liteflow.ai.model.openai.interact;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
import java.util.stream.IntStream;

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
    protected void parseStreamingToolCall(JSONObject responseJson, InteractContext context) {
        JSONObject message = extractMessage(responseJson);
        if (!message.containsKey("tool_calls")) {
            return;
        }

        JSONArray toolCallChunks = message.getJSONArray("tool_calls");

        toolCallChunks.forEach(chunkObj -> {
            JSONObject toolCallChunk = (JSONObject) chunkObj;
            JSONObject functionJson = toolCallChunk.getJSONObject("function");

            // OpenAI 的流式工具调用需要进行增量解析
            // 如果 name 不为空，那么认为这是一次新的工具调用
            if (StrUtil.isNotBlank(functionJson.getString("name"))) {
                // 添加新的工具调用到上下文
                context.addToolCall(
                        ToolCall.builder()
                                .id(toolCallChunk.getString("id"))
                                .type(toolCallChunk.getString("type"))
                                .name(functionJson.getString("name"))
                                .arguments(functionJson.getString("arguments"))
                                .build()
                );
            } else {
                // 增量解析后续的 arguments
                Optional.ofNullable(functionJson.getString("arguments"))
                        .ifPresent(context::addToolCallArguments);
            }
        });
    }

    @Override
    protected List<ToolCall> extractToolCalls(JSONObject responseJson) {
        JSONObject message = extractMessage(responseJson);
        if (!message.containsKey("tool_calls")) {
            return Collections.emptyList();
        }

        JSONArray toolCalls = message.getJSONArray("tool_calls");
        if (CollectionUtil.isEmpty(toolCalls)) {
            return Collections.emptyList();
        }

        return IntStream.range(0, toolCalls.size())
                .mapToObj(i -> {
                    JSONObject toolCallJson = toolCalls.getJSONObject(i);
                    JSONObject functionJson = toolCallJson.getJSONObject("function");

                    if (Objects.nonNull(functionJson)) {
                        return ToolCall.builder()
                                .id(toolCallJson.getString("id"))
                                .type(toolCallJson.getString("type"))
                                .name(functionJson.getString("name"))
                                .arguments(functionJson.getString("arguments"))
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    protected JSONObject extractMessage(JSONObject responseJson) {
        JSONArray choices = extractChoices(responseJson);
        if (CollectionUtil.isEmpty(choices)) {
            return null;
        }
        return Optional.ofNullable(choices.getJSONObject(0).getJSONObject("message"))
                .orElse(choices.getJSONObject(0).getJSONObject("delta"));
    }

    @Override
    protected String extractContent(JSONObject messageJson) {
        if (Objects.isNull(messageJson)) return null;
        String content = messageJson.getString("content");
        if (StrUtil.isBlank(content)) {
            content = messageJson.getString("refusal");
        }
        if (StrUtil.isBlank(content)) {
            content = messageJson.getString("reasoning_content");
        }
        return content;
    }

    @Override
    protected String extractThinkingContent(JSONObject messageJson) {
        String content = extractContent(messageJson);
        return content.replaceAll("<?think>", "");
    }

    @Override
    protected boolean isResponseDone(JSONObject responseJson) {
        JSONArray choices = extractChoices(responseJson);
        if (CollectionUtil.isEmpty(choices)) return false;
        String finishReason = choices.getJSONObject(0).getString("finish_reason");
        return StrUtil.isNotBlank(finishReason);
    }

    @Override
    protected TokenUsage extractTokenUsage(JSONObject responseJson) {
        return Optional.ofNullable(responseJson.getJSONObject("usage"))
                .map(usageJson -> {
                    Integer promptTokens = usageJson.getInteger("prompt_tokens");
                    Integer completionTokens = usageJson.getInteger("completion_tokens");
                    Integer totalTokens = usageJson.getInteger("total_tokens");
                    return new TokenUsage(promptTokens, completionTokens, totalTokens);
                })
                .orElse(null);
    }

    @Override
    protected boolean isThinkingStart(JSONObject messageJson) {
        if (Objects.isNull(messageJson)) return false;
        return Optional.ofNullable(extractContent(messageJson))
                .map(content -> content.contains("<think>"))
                .orElse(false);
    }

    @Override
    protected boolean isThinkingEnd(JSONObject messageJson) {
        if (Objects.isNull(messageJson)) return false;
        return Optional.ofNullable(extractContent(messageJson))
                .map(content -> content.contains("</think>"))
                .orElse(false);
    }

    @Override
    public String getProviderName() {
        return OpenAIConstant.PROVIDER_NAME;
    }

    private JSONArray extractChoices(JSONObject responseJson) {
        return responseJson.getJSONArray("choices");
    }
}
