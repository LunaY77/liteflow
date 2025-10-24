package com.yomahub.liteflow.ai.model.dashscope.model.chat;

import com.yomahub.liteflow.ai.engine.interact.InteractClient;
import com.yomahub.liteflow.ai.engine.model.chat.AbstractChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.dashscope.constants.DashScopeConstant;

/**
 * DashScope 聊天模型类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class DashScopeChatModel extends AbstractChatModel {

    public DashScopeChatModel(ChatConfig config) {
        super(config);
    }

    public DashScopeChatModel(ChatConfig config, InteractClient interactClient) {
        super(config, interactClient);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractChatModel.Builder<Builder> {

        private final DashScopeChatConfig.Builder configBuilder;

        public Builder() {
            this.configBuilder = DashScopeChatConfig.builder();
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
        public DashScopeChatModel build() {
            this.configBuilder.provider(DashScopeConstant.PROVIDER_NAME);
            return new DashScopeChatModel(this.configBuilder.build(), this.interactClient);
        }
    }
}
