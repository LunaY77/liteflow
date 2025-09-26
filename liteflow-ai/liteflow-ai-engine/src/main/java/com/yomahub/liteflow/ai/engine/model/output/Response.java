package com.yomahub.liteflow.ai.engine.model.output;

import com.yomahub.liteflow.ai.engine.model.ModelResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 响应
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class Response<T> implements ModelResponse<T> {

    protected final T output;
    protected final TokenUsage tokenUsage;
    protected final FinishReason finishReason;
    protected final Map<String, Object> metadata;

    public Response(T output) {
        this(output, null, null, new HashMap<>());
    }

    public Response(T output, TokenUsage tokenUsage, FinishReason finishReason) {
        this(output, tokenUsage, finishReason, new HashMap<>());
    }

    public Response(T output,
                    TokenUsage tokenUsage,
                    FinishReason finishReason,
                    Map<String, Object> metadata
    ) {
        Objects.requireNonNull(output);
        this.output = output;
        this.tokenUsage = tokenUsage;
        this.finishReason = finishReason;
        this.metadata = Optional.ofNullable(metadata)
                .map(HashMap::new)
                .orElseGet(HashMap::new);
    }

    public Response(Builder<T, ?> builder) {
        this.output = builder.content;
        this.tokenUsage = builder.tokenUsage;
        this.finishReason = builder.finishReason;
        this.metadata = new HashMap<>(builder.metadata);
    }

    @Override
    public T getOutput() {
        return output;
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
