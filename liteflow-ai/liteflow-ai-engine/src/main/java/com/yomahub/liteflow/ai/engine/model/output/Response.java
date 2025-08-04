package com.yomahub.liteflow.ai.engine.model.output;

import com.yomahub.liteflow.ai.engine.model.ModelResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 响应
 *
 * @author 苍镜月
 * @since TODO
 */

public class Response<T> implements ModelResponse<T> {

    protected final T content;
    protected final TokenUsage tokenUsage;
    protected final FinishReason finishReason;
    protected final Map<String, Object> metadata;

    public Response(T content) {
        this(content, null, null, new HashMap<>());
    }

    public Response(T content, TokenUsage tokenUsage, FinishReason finishReason) {
        this(content, tokenUsage, finishReason, new HashMap<>());
    }

    public Response(T content,
                    TokenUsage tokenUsage,
                    FinishReason finishReason,
                    Map<String, Object> metadata
    ) {
        Objects.requireNonNull(content);
        this.content = content;
        this.tokenUsage = tokenUsage;
        this.finishReason = finishReason;
        this.metadata = new HashMap<>(metadata);
    }

    public Response(Builder<T, ?> builder) {
        this.content = builder.content;
        this.tokenUsage = builder.tokenUsage;
        this.finishReason = builder.finishReason;
        this.metadata = new HashMap<>(builder.metadata);
    }

    @Override
    public T getContent() {
        return content;
    }

    public TokenUsage getTokenUsage() {
        return tokenUsage;
    }

    public FinishReason getFinishReason() {
        return finishReason;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static <T> Builder<T, ?> builder() {
        return new Builder.BuilderImpl<>();
    }

    public static abstract class Builder<T, B extends Builder<T, B>> {
        protected T content;
        protected TokenUsage tokenUsage;
        protected FinishReason finishReason;
        protected Map<String, Object> metadata = new HashMap<>();

        public B content(T content) {
            this.content = content;
            return self();
        }

        public B tokenUsage(TokenUsage tokenUsage) {
            this.tokenUsage = tokenUsage;
            return self();
        }

        public B finishReason(FinishReason finishReason) {
            this.finishReason = finishReason;
            return self();
        }

        public B metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return self();
        }

        protected abstract B self();

        public abstract Response<T> build();

        private static class BuilderImpl<T> extends Builder<T, BuilderImpl<T>> {
            @Override
            protected BuilderImpl<T> self() {
                return this;
            }

            @Override
            public Response<T> build() {
                return new Response<>(this);
            }
        }
    }
}
