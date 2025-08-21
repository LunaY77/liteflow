package com.yomahub.liteflow.ai.model.openai.config;

import com.yomahub.liteflow.ai.config.LiteFlowAIModelProperty;
import com.yomahub.liteflow.ai.model.openai.constants.OpenAIConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OpenAI 模型参数配置
 *
 * @author 苍镜月
 * @since TODO
 */

@ConfigurationProperties(prefix = "liteflow.ai.openai")
public class OpenAIModelProperty implements LiteFlowAIModelProperty {

    private String apiKey;

    @Override
    public String getProviderName() {
        return OpenAIConstant.PROVIDER_NAME;
    }

    @Override
    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
