package com.yomahub.liteflow.ai.model;

import com.yomahub.liteflow.ai.domain.ModelConfig;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模型工厂
 *
 * @author 苍镜月
 * @since TODO
 */

public class ModelFactory {

    private static final LFLog LOG = LFLoggerManager.getLogger(ModelFactory.class);

    private static final Map<String, ModelProvider> MODEL_PROVIDER_MAP = new ConcurrentHashMap<>();

    // 模型类型缓存
    private static final Map<ModelConfig, ChatModel> CHAT_MODEL_CACHE = new ConcurrentHashMap<>();
    private static final Map<ModelConfig, StreamingChatModel> STREAMING_CHAT_MODEL_CACHE = new ConcurrentHashMap<>();
    private static final Map<ModelConfig, EmbeddingModel> EMBEDDING_MODEL_CACHE = new ConcurrentHashMap<>();

    // 私有化构造函数
    private ModelFactory() {
    }

    /**
     * 注册模型提供者
     *
     * @param provider 模型提供者
     */
    public static void register(ModelProvider provider) {
        Objects.requireNonNull(provider, "ModelProvider cannot be null");
        Objects.requireNonNull(provider.getProviderName(), "Provider name cannot be null");
        MODEL_PROVIDER_MAP.put(provider.getProviderName().toLowerCase(), provider);
        LOG.info("Registered model provider: {}", provider.getProviderName());
    }

    /**
     * 获取指定提供者名称的ChatModel实例
     *
     * @param config 模型配置
     * @return ChatModel实例
     */
    public static ChatModel getChatModel(ModelConfig config) {
        return CHAT_MODEL_CACHE.computeIfAbsent(config, key -> {
            ModelProvider provider = getProvider(key.getProvider());
            // 创建 ChatModel 实例
            return provider.createChatModel(key)
                    .orElseThrow(() -> new LiteFlowAIException("ChatModel is not supported for provider: " + key.getProvider()));
        });
    }

    /**
     * 获取指定提供者名称的StreamingChatModel实例
     *
     * @param config 模型配置
     * @return StreamingChatModel实例
     */
    public static StreamingChatModel getStreamingChatModel(ModelConfig config) {
        return STREAMING_CHAT_MODEL_CACHE.computeIfAbsent(config, key -> {
            ModelProvider provider = getProvider(config.getProvider());
            // 创建 StreamingChatModel 实例
            return provider.createStreamingChatModel(config)
                    .orElseThrow(() -> new LiteFlowAIException("StreamingChatModel is not supported for provider: " + key.getProvider()));
        });
    }

    /**
     * 获取指定提供者名称的EmbeddingModel实例
     *
     * @param config 模型配置
     * @return EmbeddingModel实例
     */
    public static EmbeddingModel getEmbeddingModel(ModelConfig config) {
        return EMBEDDING_MODEL_CACHE.computeIfAbsent(config, key -> {
            ModelProvider provider = getProvider(key.getProvider());
            // 创建 EmbeddingModel 实例
            return provider.createEmbeddingModel(config)
                    .orElseThrow(() -> new LiteFlowAIException("EmbeddingModel is not supported for provider: " + key.getProvider()));
        });
    }

    /**
     * 获取所有已注册的模型提供者名称
     *
     * @return 已注册的模型提供者名称集合
     */
    public static Set<String> getRegisteredProviders() {
        return MODEL_PROVIDER_MAP.keySet();
    }

    /**
     * 获取指定提供者名称的ModelProvider实例
     *
     * @param providerName 模型提供者名称
     * @return ModelProvider实例
     */
    private static ModelProvider getProvider(String providerName) {
        ModelProvider provider = MODEL_PROVIDER_MAP.get(providerName.toLowerCase());
        if (Objects.isNull(provider)) {
            throw new LiteFlowAIException("ModelProvider not found for name: " + providerName);
        }
        return provider;
    }
}
