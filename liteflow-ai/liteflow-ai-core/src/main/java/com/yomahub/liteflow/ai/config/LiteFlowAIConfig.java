package com.yomahub.liteflow.ai.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LiteFlow AI 配置类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class LiteFlowAIConfig {

    private boolean enable;

    private List<String> basePackages;

    private Map<String, ModelProviderConfig> providers = new HashMap<>();

    public boolean isEnable() {
        return enable;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

    public Map<String, ModelProviderConfig> getProviders() {
        return providers;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public void setProviders(Map<String, ModelProviderConfig> providers) {
        this.providers = providers;
    }

    /**
     * AI 模型提供商配置
     */
    public static class ModelProviderConfig {
        private String apiKey;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }
}
