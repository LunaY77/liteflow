package com.yomahub.liteflow.ai.model.ollama.model;

import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.embedding.EmbeddingModel;
import com.yomahub.liteflow.ai.model.ModelProviderRegistrar;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatConfig;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;

import java.util.Optional;

/**
 * Ollama 模型提供者
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaModelProvider extends ModelProviderRegistrar {

    @Override
    public Optional<ChatModel> createChatModel(AIProxyWrapBean<?> wrapBean) {
        return Optional.of(new OllamaChatModel(OllamaChatConfig
                .builder()
                .apiUrl(OllamaConstant.DEFAULT_API_URL)
                .endPoint(OllamaConstant.DEFAULT_END_POINT)
                .provider(OllamaConstant.PROVIDER_NAME)
                .model("qwen3:32b")
                .build()));
    }

    @Override
    public Optional<EmbeddingModel> createEmbeddingModel(AIProxyWrapBean<?> wrapBean) {
        return super.createEmbeddingModel(wrapBean);
    }

    @Override
    public String getProviderName() {
        return OllamaConstant.PROVIDER_NAME;
    }
}
