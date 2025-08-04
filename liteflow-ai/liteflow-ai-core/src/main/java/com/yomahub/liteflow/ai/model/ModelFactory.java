package com.yomahub.liteflow.ai.model;

import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.embedding.EmbeddingModel;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

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

    // 模型类型缓存 key: nodeId, value: 模型实例
    private static final Map<String, ChatModel> CHAT_MODEL_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, EmbeddingModel> EMBEDDING_MODEL_CACHE = new ConcurrentHashMap<>();

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
     * @param wrapBean AI 节点包装 Bean，从中获取模型配置信息
     * @return ChatModel实例
     */
    public static ChatModel getChatModel(AIProxyWrapBean<?> wrapBean) {
        return CHAT_MODEL_CACHE.computeIfAbsent(wrapBean.getNodeId(), id -> {
            String providerName = wrapBean.getConfig().getProvider();
            ModelProvider provider = getProvider(providerName);
            // 创建 ChatModel 实例
            return provider.createChatModel(wrapBean)
                    .orElseThrow(() -> new LiteFlowAIException("ChatModel is not supported for provider: " + providerName));
        });
    }

    /**
     * 获取指定提供者名称的EmbeddingModel实例
     *
     * @param wrapBean AI 节点包装 Bean，从中获取模型配置信息
     * @return EmbeddingModel实例
     */
    public static EmbeddingModel getEmbeddingModel(AIProxyWrapBean<?> wrapBean) {
        return EMBEDDING_MODEL_CACHE.computeIfAbsent(wrapBean.getNodeId(), id -> {
            String providerName = wrapBean.getConfig().getProvider();
            ModelProvider provider = getProvider(providerName);
            // 创建 EmbeddingModel 实例
            return provider.createEmbeddingModel(wrapBean)
                    .orElseThrow(() -> new LiteFlowAIException("EmbeddingModel is not supported for provider: " + providerName));
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
