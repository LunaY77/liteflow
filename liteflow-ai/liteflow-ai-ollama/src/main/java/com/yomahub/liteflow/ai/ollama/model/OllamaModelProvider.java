package com.yomahub.liteflow.ai.ollama.model;

import com.yomahub.liteflow.ai.domain.ModelConfig;
import com.yomahub.liteflow.ai.domain.constant.ProviderName;
import com.yomahub.liteflow.ai.model.ModelProviderRegistrar;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import java.util.Optional;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaModelProvider extends ModelProviderRegistrar {

    @Override
    public String getProviderName() {
        return ProviderName.OLLAMA;
    }

    @Override
    public Optional<ChatModel> createChatModel(ModelConfig config) {
        // TODO
        return Optional.of(
            OllamaChatModel.builder()
                .baseUrl(config.getBaseUrl())
                .modelName(config.getModel())
                .build()
        );
    }

    @Override
    public Optional<StreamingChatModel> createStreamingChatModel(ModelConfig config) {
        // TODO
        return Optional.of(
                OllamaStreamingChatModel.builder()
                        .baseUrl(config.getBaseUrl())
                        .modelName(config.getModel())
                        .build()
        );
    }
}
