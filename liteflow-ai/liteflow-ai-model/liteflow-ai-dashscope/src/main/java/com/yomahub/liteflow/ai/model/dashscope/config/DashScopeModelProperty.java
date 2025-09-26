package com.yomahub.liteflow.ai.model.dashscope.config;

import com.yomahub.liteflow.ai.config.LiteFlowAIModelProperty;
import com.yomahub.liteflow.ai.model.dashscope.constants.DashScopeConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DashScope 模型参数配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@ConfigurationProperties(prefix = "liteflow.ai.dashscope")
public class DashScopeModelProperty implements LiteFlowAIModelProperty {

    private String apiKey;

    @Override
    public String getProviderName() {
        return DashScopeConstant.PROVIDER_NAME;
    }

    @Override
    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
