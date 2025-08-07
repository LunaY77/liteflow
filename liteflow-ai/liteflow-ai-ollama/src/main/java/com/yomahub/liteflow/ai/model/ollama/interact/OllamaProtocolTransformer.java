package com.yomahub.liteflow.ai.model.ollama.interact;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yomahub.liteflow.ai.engine.interact.protocol.AbstractProtocolTransformer;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;

import static com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant.PROVIDER_NAME;

/**
 * Ollama 协议转换器
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaProtocolTransformer extends AbstractProtocolTransformer {

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
