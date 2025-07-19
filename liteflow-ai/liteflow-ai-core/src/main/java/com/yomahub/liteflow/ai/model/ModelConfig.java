package com.yomahub.liteflow.ai.model;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 模型配置信息
 *
 * @author 苍镜月
 * @since TODO
 */

public class ModelConfig implements RequestBodyConvertible {

    protected String apiUrl;

    protected String endPoint;

    protected String apiKey;

    protected String provider;

    protected String model;

    protected Duration timeout = Duration.ofSeconds(60);

    protected Map<String, String> headersConfig = new LinkedHashMap<>();

    protected static final String MODEL_KEY = "model";

    public ModelConfig() {
    }

    public ModelConfig(
            String apiUrl,
            String endPoint,
            String apiKey,
            String provider,
            String model,
            Duration timeout,
            Map<String, String> headersConfig
    ) {
        this.apiUrl = apiUrl;
        this.endPoint = endPoint;
        this.apiKey = apiKey;
        this.provider = provider;
        this.model = model;
        this.timeout = timeout;
        this.headersConfig = headersConfig;
    }

    protected ModelConfig(Builder<?> builder) {
        this.apiUrl = builder.apiUrl;
        this.endPoint = builder.endPoint;
        this.apiKey = builder.apiKey;
        this.provider = builder.provider;
        this.model = builder.model;
        this.timeout = builder.timeout;
        this.headersConfig.putAll(builder.headersConfig);
    }

    @Override
    public RequestBody toRequestBody() {
        return RequestBody.of()
                .putIfNotNull(MODEL_KEY, model);
    }

    /**
     * 解析完整的API URL
     *
     * @return 完整的API URL
     */
    public String resolveUrl() {
        String cleanApiUrl = apiUrl.endsWith("/") ? apiUrl.substring(0, apiUrl.length() - 1) : apiUrl;
        String cleanEndpoint = endPoint.startsWith("/") ? endPoint.substring(1) : endPoint;

        return cleanApiUrl + "/" + cleanEndpoint;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
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

    public static Builder<?> builder() {
        return new Builder.BuilderImpl();
    }

    public static abstract class Builder<B extends Builder<B>> {
        protected String apiUrl;

        protected String endPoint;

        protected String apiKey;

        protected String provider;

        protected String model;

        protected Duration timeout = Duration.ofSeconds(60);

        protected Map<String, String> headersConfig = new LinkedHashMap<>();

        protected abstract B self();

        public B apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return self();
        }

        public B endPoint(String endPoint) {
            this.endPoint = endPoint;
            return self();
        }

        public B apiKey(String apiKey) {
            this.apiKey = apiKey;
            return self();
        }

        public B provider(String provider) {
            this.provider = provider;
            return self();
        }

        public B model(String model) {
            this.model = model;
            return self();
        }

        public B timeout(Duration timeout) {
            this.timeout = timeout;
            return self();
        }

        public B headersConfig(Map<String, String> headersConfig) {
            this.headersConfig = headersConfig;
            return self();
        }

        protected void checkRequiredFields() {
            Objects.requireNonNull(apiUrl, "API URL must not be null");
            Objects.requireNonNull(endPoint, "End Point must not be null");
            Objects.requireNonNull(provider, "Provider must not be null");
            Objects.requireNonNull(model, "Model must not be null");
        }

        public abstract ModelConfig build();

        private static class BuilderImpl extends Builder<BuilderImpl> {

            @Override
            protected BuilderImpl self() {
                return this;
            }

            @Override
            public ModelConfig build() {
                checkRequiredFields();
                return new ModelConfig(this);
            }
        }
    }
}
