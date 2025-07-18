package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.interact.InteractClient;
import com.yomahub.liteflow.ai.interact.LlmInteractClient;
import com.yomahub.liteflow.ai.model.chat.ChatModel;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.model.runtime.LiteFlowAIModel;

import java.util.concurrent.CompletableFuture;

import static com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant.PROVIDER_NAME;
import static com.yomahub.liteflow.ai.model.runtime.ModelType.CHAT_MODEL;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@LiteFlowAIModel(PROVIDER_NAME + CHAT_MODEL)
public class OllamaChatModel implements ChatModel {

    private final OllamaChatConfig config;
    private final InteractClient interactClient;

    public OllamaChatModel(OllamaChatConfig config) {
        this.config = config;
        this.interactClient = new LlmInteractClient();
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        return interactClient.chat(config, request);
    }

    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatRequest request) {
        return null;
    }

    @Override
    public void stream(ChatRequest request) {

    }

    @Override
    public OllamaChatConfig getModelConfig() {
        return config;
    }
}
