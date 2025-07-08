package com.yomahub.liteflow.ai.model;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模型配置信息
 *
 * @author 苍镜月
 * @since TODO
 */

public class ModelConfig {

    protected String apiUrl;

    protected String apiKey;

    protected String provider;

    protected String model;

    protected Duration timeout = Duration.ofSeconds(60);

    protected final Map<String, String> headersConfig = new LinkedHashMap<>();

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Map<String, String> getHeadersConfig() {
        return headersConfig;
    }

    public void addHeader(String key, String value) {
        this.headersConfig.put(key, value);
    }

    public void removeHeader(String key) {
        this.headersConfig.remove(key);
    }
}
