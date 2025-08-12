package com.yomahub.liteflow.ai.engine.model.chat.entity;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.Response;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.parser.OutputParser;

import java.lang.reflect.Type;
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

    /**
     * 将响应内容转换为指定类型
     *
     * @param parser 目标类型解析器
     * @param <T>    目标类型
     * @return 转换后的对象
     */
    public <T> T as(OutputParser<T> parser) {
        String rawTextContent = this.getContent().getContent();
        if (StrUtil.isBlank(rawTextContent)) {
            throw new IllegalStateException("Cannot convert empty content to target type: " + parser.getTargetType());
        }

        return parser.convert(rawTextContent);
    }

    /**
     * 将响应内容转换为指定类型
     *
     * @param targetType 目标类型
     * @param <T>        目标类型
     * @return 转换后的对象
     */
    public <T> T as(TypeReference<T> targetType) {
        OutputParser<T> parser = OutputParser.fromTypeReference(targetType);
        return this.as(parser);
    }

    /**
     * 将响应内容转换为指定类型
     *
     * @param targetType 目标类型
     * @param <T>        目标类型
     * @return 转换后的对象
     */
    public <T> T as(Type targetType) {
        return this.as(new TypeReference<T>(targetType.getTypeName()) {
        });
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
