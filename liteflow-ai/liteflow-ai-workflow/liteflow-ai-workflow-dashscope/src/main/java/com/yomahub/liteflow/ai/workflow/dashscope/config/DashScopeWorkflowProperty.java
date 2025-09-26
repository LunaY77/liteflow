package com.yomahub.liteflow.ai.workflow.dashscope.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里百炼工作流配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@ConfigurationProperties(prefix = "liteflow.ai.workflow.dashscope")
public class DashScopeWorkflowProperty {

    private String apiKey;

    private String apiUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
