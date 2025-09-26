package com.yomahub.liteflow.ai.model;

import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;

import java.util.Optional;

/**
 * 模型提供者接口
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface ModelProvider {

    /**
     * 获取模型提供者名称(e.g. "azure-open-ai", "openai", "google-palm", "anthropic")
     *
     * @return 模型提供者名称
     */
    String getProviderName();

    /**
     * 创建ChatRequest构建器
     *
     * @return ChatRequest 的建造者
     */
    default Optional<ChatRequest.Builder<?>> createChatRequestBuilder() {
        return Optional.of(ChatRequest.builder());
    }

    /**
     * 创建ChatModel实例
     *
     * @param configAggregator 模型配置聚合信息
     * @return ChatModel实例
     */
    default Optional<ChatModel> createChatModel(ModelConfigAggregator configAggregator) {
        return Optional.empty();
    }
}
