package com.yomahub.liteflow.ai.model.ollama.interact;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.AbstractProtocolTransformer;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant.PROVIDER_NAME;

/**
 * Ollama 协议转换器
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaProtocolTransformer extends AbstractProtocolTransformer {

    @Override
    protected void parseStreamingToolCall(JSONObject responseJson, InteractContext context) {
        // ollama 的工具调用信息在 message 中
        JSONObject message = extractMessage(responseJson);
        List<ToolCall> toolCalls = extractToolCallsFromMessage(message);
        if (CollectionUtil.isNotEmpty(toolCalls)) {
            context.setToolCalls(toolCalls);
        }
    }

    @Override
    protected List<ToolCall> extractToolCalls(JSONObject responseJson) {
        JSONObject message = extractMessage(responseJson);
        return extractToolCallsFromMessage(message);
    }

    private List<ToolCall> extractToolCallsFromMessage(JSONObject messageJson) {
        if (!messageJson.containsKey("tool_calls")) {
            return Collections.emptyList();
        }

        JSONArray toolCalls = messageJson.getJSONArray("tool_calls");
        if (CollectionUtil.isEmpty(toolCalls)) {
            return Collections.emptyList();
        }

        return IntStream.range(0, toolCalls.size())
                .mapToObj(i -> {
                    JSONObject toolCallJson = toolCalls.getJSONObject(i);
                    JSONObject functionJson = toolCallJson.getJSONObject("function");

                    if (Objects.nonNull(functionJson)) {
                        return ToolCall.builder()
                                .type("function")
                                .name(functionJson.getString("name"))
                                .arguments(functionJson.getJSONObject("arguments"))
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    protected JSONObject extractMessage(JSONObject responseJson) {
        return responseJson.getJSONObject("message");
    }

    @Override
    protected String extractContent(JSONObject messageJson) {
        return messageJson.getString("content");
    }

    @Override
    protected String extractThinkingContent(JSONObject messageJson) {
        String thinking = messageJson.getString("thinking");
        String content;
        if (StrUtil.isNotBlank(thinking)) {
            content = thinking;
        } else {
            content = extractContent(messageJson);
        }
        return content.replaceAll("<?think>", "");
    }

    @Override
    protected boolean isResponseDone(JSONObject responseJson) {
        return responseJson.getBooleanValue("done", false);
    }

    @Override
    protected TokenUsage extractTokenUsage(JSONObject responseJson) {
        Integer promptTokens = responseJson.getInteger("prompt_eval_count");
        Integer completionTokens = responseJson.getInteger("eval_count");
        // 计算 Token 使用情况
        return new TokenUsage(promptTokens, completionTokens);
    }

    @Override
    protected boolean isThinkingStart(JSONObject messageJson) {
        return messageJson.getString("content").contains("<think>") ||
                StrUtil.isNotBlank(messageJson.getString("thinking"));
    }

    @Override
    protected boolean isThinkingEnd(JSONObject messageJson) {
        return messageJson.getString("content").contains("</think>") ||
                StrUtil.isBlank(messageJson.getString("thinking"));
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
}
