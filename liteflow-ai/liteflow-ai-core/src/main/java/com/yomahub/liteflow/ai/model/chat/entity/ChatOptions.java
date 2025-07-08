package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.model.ModelOptions;

/**
 * 对话选项配置
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatOptions implements ModelOptions {

    public static final ChatOptions DEFAULT = new ChatOptions() {

    };

    private Float temperature;

    private Float topP;

    private Float topK;

    private Integer maxTokens;

    private String seed;

    private Boolean enableThinking;

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
}
