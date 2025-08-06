package com.yomahub.liteflow.ai.engine.model.chat.entity;

import com.yomahub.liteflow.ai.engine.model.ModelOptions;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;

/**
 * 对话选项配置
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatOptions implements ModelOptions {

    public static final ChatOptions DEFAULT = ChatOptions.builder()
            .temperature(0.8)
            .topP(0.9)
            .topK(50)
            .maxTokens(512)
            .seed(null)
            .enableThinking(false)
            .build();

    protected Double temperature;

    protected Double topP;

    protected Integer topK;

    protected Integer maxTokens;

    protected Integer seed;

    protected Boolean enableThinking;

    protected static final String TEMPERATURE_KEY = "options.temperature";
    protected static final String TOP_P_KEY = "options.top_p";
    protected static final String TOP_K_KEY = "options.top_k";
    protected static final String SEED_KEY = "options.seed";
    protected static final String THINK_KEY = "think";

    public ChatOptions() {
        this.temperature = DEFAULT.temperature;
        this.topP = DEFAULT.topP;
        this.topK = DEFAULT.topK;
        this.maxTokens = DEFAULT.maxTokens;
        this.seed = DEFAULT.seed;
        this.enableThinking = DEFAULT.enableThinking;
    }

    public ChatOptions(
            Double temperature,
            Double topP,
            Integer topK,
            Integer maxTokens,
            Integer seed,
            Boolean enableThinking
    ) {
        this.temperature = temperature;
        this.topP = topP;
        this.topK = topK;
        this.maxTokens = maxTokens;
        this.seed = seed;
        this.enableThinking = enableThinking;
    }

    public ChatOptions(Builder<?> builder) {
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

    public Double getTemperature() {
        return temperature;
    }

    public Double getTopP() {
        return topP;
    }

    public Integer getTopK() {
        return topK;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public Integer getSeed() {
        return seed;
    }

    public Boolean getEnableThinking() {
        return enableThinking;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public void setTopK(Integer topK) {
        this.topK = topK;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public void setEnableThinking(Boolean enableThinking) {
        this.enableThinking = enableThinking;
    }

    public static Builder<?> builder() {
        return new Builder.BuilderImpl();
    }

    public static abstract class Builder<B extends Builder<B>> {
        protected Double temperature;
        protected Double topP;
        protected Integer topK;
        protected Integer maxTokens;
        protected Integer seed;
        protected Boolean enableThinking;

        protected abstract B self();

        public abstract ChatOptions build();

        public B temperature(Double temperature) {
            this.temperature = temperature;
            return self();
        }

        public B topP(Double topP) {
            this.topP = topP;
            return self();
        }

        public B topK(Integer topK) {
            this.topK = topK;
            return self();
        }

        public B maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return self();
        }

        public B seed(Integer seed) {
            this.seed = seed;
            return self();
        }

        public B enableThinking(Boolean enableThinking) {
            this.enableThinking = enableThinking;
            return self();
        }

        private static class BuilderImpl extends Builder<BuilderImpl> {
            @Override
            protected BuilderImpl self() {
                return this;
            }

            @Override
            public ChatOptions build() {
                return new ChatOptions(this);
            }
        }
    }
}
