package com.yomahub.liteflow.ai.model.ollama.config;

import com.yomahub.liteflow.ai.config.LiteFlowAIModelProperty;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ollama 模型参数配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@ConfigurationProperties(prefix = "liteflow.ai.ollama")
public class OllamaModelProperty implements LiteFlowAIModelProperty {

    private String apiKey;

    @Override
    public String getProviderName() {
        return OllamaConstant.PROVIDER_NAME;
    }

    @Override
    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
