package com.yomahub.liteflow.ai.config;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.util.KeyValue;
import com.yomahub.liteflow.ai.util.TriState;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 模型配置
 *
 * @author 苍镜月
 * @since TODO
 */

public final class ModelConfig {

    private final String provider;
    private final String baseUrl;
    private final String model;
    private final String apiKey;
    private final String version;
    private final String timeout;
    private final int maxRetries;
    private final double temperature;
    private final double topP;
    private final int topK;
    private final int maxTokens;
    private final List<String> stop;
    private final int seed;
    private final double repeatPenalty;
    private final double presencePenalty;
    private final double frequencyPenalty;
    private final String responseFormat;
    private final TriState strictJsonSchema;
    private final TriState parallelToolCalls;
    private final TriState logRequests;
    private final TriState logResponses;
    private final List<KeyValue> customHeaders;

    public ModelConfig(String provider, String baseUrl, String model, String apiKey, String version,
                       String timeout, int maxRetries, double temperature, double topP, int topK,
                       int maxTokens, List<String> stop, int seed, double repeatPenalty,
                       double presencePenalty, double frequencyPenalty, String responseFormat,
                       TriState strictJsonSchema, TriState parallelToolCalls, TriState logRequests,
                       TriState logResponses, List<KeyValue> customHeaders) {
        this.provider = provider;
        this.baseUrl = baseUrl;
        this.model = model;
        this.apiKey = apiKey;
        this.version = version;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.temperature = temperature;
        this.topP = topP;
        this.topK = topK;
        this.maxTokens = maxTokens;
        this.stop = stop;
        this.seed = seed;
        this.repeatPenalty = repeatPenalty;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.responseFormat = responseFormat;
        this.strictJsonSchema = strictJsonSchema;
        this.parallelToolCalls = parallelToolCalls;
        this.logRequests = logRequests;
        this.logResponses = logResponses;
        this.customHeaders = customHeaders;
    }

    public static ModelConfig fromAnnotation(AIComponent anno) {
        return new ModelConfig(
                anno.provider(), anno.baseUrl(), anno.model(), anno.apiKey(), anno.version(), anno.timeout(), anno.maxRetries(),
                anno.temperature(), anno.topP(), anno.topK(), anno.maxTokens(), Arrays.asList(anno.stop()),
                anno.seed(), anno.repeatPenalty(), anno.presencePenalty(), anno.frequencyPenalty(), anno.responseFormat(),
                anno.strictJsonSchema(), anno.parallelToolCalls(), anno.logRequests(), anno.logResponses(),
                Arrays.asList(anno.customHeaders())
        );
    }

    // 3. 为所有字段生成 Getter 方法
    public String getProvider() {
        return provider;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getModel() {
        return model;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getVersion() {
        return version;
    }

    public String getTimeout() {
        return timeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTopP() {
        return topP;
    }

    public int getTopK() {
        return topK;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public List<String> getStop() {
        return stop;
    }

    public int getSeed() {
        return seed;
    }

    public double getRepeatPenalty() {
        return repeatPenalty;
    }

    public double getPresencePenalty() {
        return presencePenalty;
    }

    public double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    public TriState getStrictJsonSchema() {
        return strictJsonSchema;
    }

    public TriState getParallelToolCalls() {
        return parallelToolCalls;
    }

    public TriState getLogRequests() {
        return logRequests;
    }

    public TriState getLogResponses() {
        return logResponses;
    }

    public List<KeyValue> getCustomHeaders() {
        return customHeaders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelConfig that = (ModelConfig) o;
        return maxRetries == that.maxRetries &&
                Double.compare(that.temperature, temperature) == 0 &&
                Double.compare(that.topP, topP) == 0 &&
                topK == that.topK &&
                maxTokens == that.maxTokens &&
                seed == that.seed &&
                Double.compare(that.repeatPenalty, repeatPenalty) == 0 &&
                Double.compare(that.presencePenalty, presencePenalty) == 0 &&
                Double.compare(that.frequencyPenalty, frequencyPenalty) == 0 &&
                Objects.equals(provider, that.provider) &&
                Objects.equals(baseUrl, that.baseUrl) &&
                Objects.equals(model, that.model) &&
                Objects.equals(apiKey, that.apiKey) &&
                Objects.equals(version, that.version) &&
                Objects.equals(timeout, that.timeout) &&
                Objects.equals(stop, that.stop) &&
                Objects.equals(responseFormat, that.responseFormat) &&
                strictJsonSchema == that.strictJsonSchema &&
                parallelToolCalls == that.parallelToolCalls &&
                logRequests == that.logRequests &&
                logResponses == that.logResponses &&
                Objects.equals(customHeaders, that.customHeaders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, baseUrl, model, apiKey, version, timeout, maxRetries, temperature, topP, topK, maxTokens, stop, seed, repeatPenalty, presencePenalty, frequencyPenalty, responseFormat, strictJsonSchema, parallelToolCalls, logRequests, logResponses, customHeaders);
    }

    @Override
    public String toString() {
        return "ModelConfig{" +
                "provider='" + provider + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", model='" + model + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", version='" + version + '\'' +
                ", timeout='" + timeout + '\'' +
                ", maxRetries=" + maxRetries +
                ", temperature=" + temperature +
                ", topP=" + topP +
                ", topK=" + topK +
                ", maxTokens=" + maxTokens +
                ", stop=" + stop +
                ", seed=" + seed +
                ", repeatPenalty=" + repeatPenalty +
                ", presencePenalty=" + presencePenalty +
                ", frequencyPenalty=" + frequencyPenalty +
                ", responseFormat='" + responseFormat + '\'' +
                ", strictJsonSchema=" + strictJsonSchema +
                ", parallelToolCalls=" + parallelToolCalls +
                ", logRequests=" + logRequests +
                ", logResponses=" + logResponses +
                ", customHeaders=" + customHeaders +
                '}';
    }
}