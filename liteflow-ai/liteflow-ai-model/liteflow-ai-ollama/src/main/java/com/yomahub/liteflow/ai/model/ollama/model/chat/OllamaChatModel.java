package com.yomahub.liteflow.ai.model.ollama.model.chat;

import com.yomahub.liteflow.ai.engine.interact.InteractClient;
import com.yomahub.liteflow.ai.engine.model.chat.AbstractChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;

/**
 * Ollama 聊天模型
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class OllamaChatModel extends AbstractChatModel {

    public OllamaChatModel(ChatConfig config) {
        super(config);
    }

    public OllamaChatModel(ChatConfig config, InteractClient interactClient) {
        super(config, interactClient);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractChatModel.Builder<Builder> {

        private final OllamaChatConfig.Builder configBuilder;

        public Builder() {
            this.configBuilder = OllamaChatConfig.builder();
        }

        @Override
        protected ChatConfig.Builder<?> getConfigBuilder() {
            return this.configBuilder;
        }

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public ChatModel build() {
            return new OllamaChatModel(this.configBuilder.build(), this.interactClient);
        }
    }
}
