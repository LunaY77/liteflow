package com.yomahub.liteflow.ai.model;

import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

import java.util.Optional;

/**
 * 模型提供者接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ModelProvider {

    /**
     * 获取模型提供者名称(e.g. "azure-open-ai", "openai", "google-palm", "anthropic")
     *
     * @return 模型提供者名称
     */
    String getProviderName();

    /**
     * 创建ChatModel实例
     *
     * @param wrapBean AI 节点包装 Bean，从中获取模型配置信息
     * @return ChatModel实例
     */
    default Optional<ChatModel> createChatModel(AIProxyWrapBean<?> wrapBean) {
        return Optional.empty();
    }

    /**
     * 创建StreamingChatModel实例
     *
     * @param wrapBean AI 节点包装 Bean，从中获取模型配置信息
     * @return StreamingChatModel实例
     */
    default Optional<StreamingChatModel> createStreamingChatModel(AIProxyWrapBean<?> wrapBean) {
        return Optional.empty();
    }

    /**
     * 创建EmbeddingModel实例
     *
     * @param wrapBean AI 节点包装 Bean，从中获取模型配置信息
     * @return EmbeddingModel实例
     */
    default Optional<EmbeddingModel> createEmbeddingModel(AIProxyWrapBean<?> wrapBean) {
        return Optional.empty();
    }
}
