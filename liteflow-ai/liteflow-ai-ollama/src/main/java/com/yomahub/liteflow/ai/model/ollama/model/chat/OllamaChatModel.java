package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.engine.interact.InteractClient;
import com.yomahub.liteflow.ai.engine.interact.LlmInteractClient;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Ollama 聊天模型
 *
 * @author 苍镜月
 * @since TODO
 */

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
