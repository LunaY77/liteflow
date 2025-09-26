package com.yomahub.liteflow.ai.model.openai.model.chat;

import com.yomahub.liteflow.ai.engine.interact.InteractClient;
import com.yomahub.liteflow.ai.engine.model.chat.AbstractChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;

/**
 * OpenAI 聊天模型实现类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class OpenAIChatModel extends AbstractChatModel {

    public OpenAIChatModel(ChatConfig config) {
        super(config);
    }

    public OpenAIChatModel(ChatConfig config, InteractClient interactClient) {
        super(config, interactClient);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractChatModel.Builder<Builder> {

        private final OpenAIChatConfig.Builder configBuilder;

        public Builder() {
            this.configBuilder = OpenAIChatConfig.builder();
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
        public OpenAIChatModel build() {
            return new OpenAIChatModel(this.configBuilder.build(), this.interactClient);
        }
    }
}
