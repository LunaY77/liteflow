package com.yomahub.liteflow.ai.engine.model.chat.entity;

import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.Response;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;

import java.util.Map;

/**
 * chat 响应体
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatResponse extends Response<AssistantMessage> {

    public ChatResponse(AssistantMessage content) {
        super(content);
    }

    public ChatResponse(AssistantMessage content, TokenUsage tokenUsage, FinishReason finishReason) {
        super(content, tokenUsage, finishReason);
    }

    public ChatResponse(AssistantMessage content, TokenUsage tokenUsage, FinishReason finishReason, Map<String, Object> metadata) {
        super(content, tokenUsage, finishReason, metadata);
    }

    public ChatResponse(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Response.Builder<AssistantMessage, Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Response<AssistantMessage> build() {
            return new ChatResponse(this);
        }
    }
}
