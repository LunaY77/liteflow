package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.model.ModelOptions;
import com.yomahub.liteflow.ai.model.RequestBody;

/**
 * 对话选项配置
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatOptions implements ModelOptions {

    public static final ChatOptions DEFAULT = ChatOptions.builder()
            .temperature(0.8f)
            .topP(0.9f)
            .topK(50f)
            .maxTokens(512)
            .seed(null)
            .enableThinking(false)
            .build();

    private Float temperature;

    private Float topP;

    private Float topK;

    private Integer maxTokens;

    private String seed;

    private Boolean enableThinking;

    protected static final String TEMPERATURE_KEY = "options.temperature";
    protected static final String TOP_P_KEY = "options.top_p";
    protected static final String TOP_K_KEY = "options.top_k";
    protected static final String SEED_KEY = "options.seed";
    protected static final String THINK_KEY = "think";

    public ChatOptions() {
    }

    public ChatOptions(
            Float temperature,
            Float topP,
            Float topK,
            Integer maxTokens,
            String seed,
            Boolean enableThinking
    ) {
        this.temperature = temperature;
        this.topP = topP;
        this.topK = topK;
        this.maxTokens = maxTokens;
        this.seed = seed;
        this.enableThinking = enableThinking;
    }

    public ChatOptions(Builder builder) {
        this.temperature = builder.temperature;
        this.topP = builder.topP;
        this.topK = builder.topK;
        this.maxTokens = builder.maxTokens;
        this.seed = builder.seed;
        this.enableThinking = builder.enableThinking;
    }

    @Override
    public RequestBody toRequestBody() {
        return RequestBody.of()
                .putIfNotEmpty(TEMPERATURE_KEY, temperature)
                .putIfNotEmpty(TOP_P_KEY, topP)
                .putIfNotEmpty(TOP_K_KEY, topK)
                .putIfNotEmpty(SEED_KEY, seed)
                .putIfNotEmpty(THINK_KEY, enableThinking);
    }

    public Float getTemperature() {
        return temperature;
    }

    public Float getTopP() {
        return topP;
    }

    public Float getTopK() {
        return topK;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public String getSeed() {
        return seed;
    }

    public Boolean getEnableThinking() {
        return enableThinking;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public void setTopP(Float topP) {
        this.topP = topP;
    }

    public void setTopK(Float topK) {
        this.topK = topK;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public void setEnableThinking(Boolean enableThinking) {
        this.enableThinking = enableThinking;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Float temperature;
        private Float topP;
        private Float topK;
        private Integer maxTokens;
        private String seed;
        private Boolean enableThinking;

        public Builder temperature(Float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topP(Float topP) {
            this.topP = topP;
            return this;
        }

        public Builder topK(Float topK) {
            this.topK = topK;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder seed(String seed) {
            this.seed = seed;
            return this;
        }

        public Builder enableThinking(Boolean enableThinking) {
            this.enableThinking = enableThinking;
            return this;
        }

        public ChatOptions build() {
            return new ChatOptions(temperature, topP, topK, maxTokens, seed, enableThinking);
        }
    }
}
