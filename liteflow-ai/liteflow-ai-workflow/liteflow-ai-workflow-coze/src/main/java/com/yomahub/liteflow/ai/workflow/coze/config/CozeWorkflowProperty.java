package com.yomahub.liteflow.ai.workflow.coze.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Coze 工作流配置
 *
 * @author 苍镜月
 * @since TODO
 */

@ConfigurationProperties(prefix = "liteflow.ai.workflow.coze")
public class CozeWorkflowProperty {

    private String apiKey;

    private String baseUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
