package com.yomahub.liteflow.ai.domain.dto;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.config.LiteFlowAIModelPropertyRegistry;
import com.yomahub.liteflow.ai.util.DurationUtil;
import com.yomahub.liteflow.ai.util.KeyValue;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.ai.util.TriState;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 模型配置聚合(与 {@link AIComponent} 相互映射)
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public final class ModelConfigAggregator {

    private final String provider;
    // --- 连接、鉴权参数 ---
    private final String apiUrl;
    private final String endPoint;
    private final String model;
    private final String apiKey;
    private final String version;
    // --- 核心参数 ---
    private final double temperature;
    private final double topP;
    private final int topK;
    private final int maxTokens;
    private final List<String> stop;
    private final int seed;
    // --- 惩罚参数 ---
    private final double repeatPenalty;
    private final double presencePenalty;
    private final double frequencyPenalty;
    // --- Tool Calling ----
    private final TriState parallelToolCalls;
    private final TriState autoToolCallEnabled;
    // --- 网络和日志参数 ---
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final int maxRetries;
    private final boolean logRequests;
    private final boolean logResponses;
    // --- 其他参数 ---
    private final List<KeyValue> customHeaders;
    private final boolean enableThinking;

    /**
     * 从 {@link AIComponent} 注解中解析模型配置
     *
     * @param aiComponent AIComponent 注解实例
     * @return ModelConfigAggregator 实例
     */
    public static ModelConfigAggregator parseFromAnnotation(AIComponent aiComponent) {
        return new ModelConfigAggregator(
                aiComponent.provider().getProviderName(),
                aiComponent.apiUrl(),
                aiComponent.endPoint(),
                aiComponent.model(),
                SpringUtil.getBean(LiteFlowAIModelPropertyRegistry.class)
                        .getApiKey(aiComponent.provider().getProviderName()).orElse(null),
                aiComponent.version(),
                aiComponent.temperature(),
                aiComponent.topP(),
                aiComponent.topK(),
                aiComponent.maxTokens(),
                Objects.nonNull(aiComponent.stop()) ? Arrays.asList(aiComponent.stop()) : Collections.emptyList(),
                aiComponent.seed(),
                aiComponent.repeatPenalty(),
                aiComponent.presencePenalty(),
                aiComponent.frequencyPenalty(),
                aiComponent.parallelToolCalls(),
                aiComponent.autoToolCallEnabled(),
                DurationUtil.toDuration(aiComponent.connectTimeout(), Duration.ofSeconds(60)),
                DurationUtil.toDuration(aiComponent.readTimeout(), Duration.ofSeconds(60)),
                aiComponent.maxRetries() <= 0 ? 3 : aiComponent.maxRetries(),
                aiComponent.logRequests(),
                aiComponent.logResponses(),
                Objects.nonNull(aiComponent.customHeaders()) ? Arrays.asList(aiComponent.customHeaders()) : Collections.emptyList(),
                aiComponent.enableThinking()
        );
    }

    private ModelConfigAggregator(String provider, String apiUrl, String endPoint,
                                  String model, String apiKey, String version,
                                  double temperature, double topP, int topK,
                                  int maxTokens, List<String> stop, int seed,
                                  double repeatPenalty, double presencePenalty,
                                  double frequencyPenalty, TriState parallelToolCalls,
                                  TriState autoToolCallEnabled, Duration connectTimeout,
                                  Duration readTimeout, int maxRetries, boolean logRequests,
                                  boolean logResponses, List<KeyValue> customHeaders, boolean enableThinking) {
        this.provider = provider;
        this.apiUrl = apiUrl;
        this.endPoint = endPoint;
        this.model = model;
        this.apiKey = apiKey;
        this.version = version;
        this.temperature = temperature;
        this.topP = topP;
        this.topK = topK;
        this.maxTokens = maxTokens;
        this.stop = stop;
        this.seed = seed;
        this.repeatPenalty = repeatPenalty;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.parallelToolCalls = parallelToolCalls;
        this.autoToolCallEnabled = autoToolCallEnabled;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxRetries = maxRetries;
        this.logRequests = logRequests;
        this.logResponses = logResponses;
        this.customHeaders = customHeaders;
        this.enableThinking = enableThinking;
    }

    /**
     * 获取默认的模型配置聚合实例
     */
    public static ModelConfigAggregator getDefault() {
        return new ModelConfigAggregator(
                "",
                "",
                "",
                "",
                "",
                "",
                -1.0,
                -1.0,
                -1,
                -1,
                Collections.emptyList(),
                -1,
                -1.0,
                -1.0,
                -1.0,
                TriState.UNSET,
                TriState.UNSET,
                Duration.ofSeconds(60),
                Duration.ofSeconds(60),
                -1,
                false,
                false,
                Collections.emptyList(),
                true
        );
    }

    public String getProvider() {
        return provider;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getEndPoint() {
        return endPoint;
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

    public TriState getParallelToolCalls() {
        return parallelToolCalls;
    }

    public TriState getAutoToolCallEnabled() {
        return autoToolCallEnabled;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public boolean getLogRequests() {
        return logRequests;
    }

    public boolean getLogResponses() {
        return logResponses;
    }

    public List<KeyValue> getCustomHeaders() {
        return customHeaders;
    }

    public boolean getEnableThinking() {
        return enableThinking;
    }
}
