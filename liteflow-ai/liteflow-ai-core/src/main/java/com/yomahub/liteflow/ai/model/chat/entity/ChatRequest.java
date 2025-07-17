package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.interact.pipeline.ChatContext;
import com.yomahub.liteflow.ai.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.model.ModelRequest;
import com.yomahub.liteflow.ai.model.chat.message.Message;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Chat 请求体
 *
 * @author 苍镜月
 * @since TODO
 */
public class ChatRequest implements ModelRequest {

    /**
     * 上下文消息列表
     */
    private final List<Message> messages;

    /**
     * 聊天选项配置
     */
    private final ChatOptions options;

    /**
     * 传输监听器，用于处理请求的开始和结束事件
     */
    private final TransportListener transportListener;

    /**
     * 结果处理器，用于处理消息全部发送完毕后的结果
     */
    private final ResultHandler resultHandler;

    /**
     * 分块回调，用于处理消息分块的各种事件
     */
    private final ChunkCallbackTransformer chunkCallbackTransformer;

    /**
     * 私有构造函数，使用 Builder 模式创建 ChatRequest 实例
     *
     * @param builder Builder 实例
     */
    private ChatRequest(Builder builder) {
        this.messages = builder.messages;
        this.options = builder.options;
        this.transportListener = builder.transportListener;
        this.resultHandler = builder.resultHandler;
        this.chunkCallbackTransformer = builder.chunkCallbackTransformer;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ChatOptions getOptions() {
        return options;
    }

    public TransportListener getTransportListener() {
        return transportListener;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }

    public ChunkCallbackTransformer getChunkCallback() {
        return chunkCallbackTransformer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Message> messages;
        private ChatOptions options;
        private final LlmListenerAggregator listenerAggregator = new LlmListenerAggregator();
        private TransportListener transportListener;
        private ResultHandler resultHandler;
        private ChunkCallbackTransformer chunkCallbackTransformer;

        /**
         * 设置上下文消息
         *
         * @param messages 上下文消息列表
         * @see Message
         */
        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        /**
         * 设置聊天选项
         *
         * @param options 聊天选项配置
         * @see ChatOptions
         */
        public Builder options(ChatOptions options) {
            this.options = options;
            return this;
        }

        /**
         * 请求开始时的回调方法
         *
         * @param onStart 请求开始时的回调函数
         * @see TransportListener#onStart(ChatContext)
         */
        public Builder onStart(Consumer<ChatContext> onStart) {
            listenerAggregator.onStart = onStart;
            return this;
        }

        /**
         * 请求结束时的回调方法
         *
         * @param onClose 请求结束时的回调函数
         * @see TransportListener#onClose(ChatContext)
         */
        public Builder onClose(Consumer<ChatContext> onClose) {
            listenerAggregator.onClose = onClose;
            return this;
        }

        /**
         * 文本消息的回调方法
         *
         * @param onText 文本消息的回调函数
         * @see ChunkCallbackTransformer#onText(String, ChatContext)
         */
        public Builder onText(BiFunction<String, ChatContext, String> onText) {
            listenerAggregator.onText = onText;
            return this;
        }

        /**
         * 思考消息的回调方法
         *
         * @param onThinking 思考消息的回调函数
         * @see ChunkCallbackTransformer#onThinking(String, ChatContext)
         */
        public Builder onThinking(BiFunction<String, ChatContext, String> onThinking) {
            listenerAggregator.onThinking = onThinking;
            return this;
        }

        /**
         * 工具调用消息的回调方法
         *
         * @param onToolsCalling 工具调用消息的回调函数
         * @see ChunkCallbackTransformer#onToolsCalling(Object, ChatContext)
         */
        public Builder onToolsCalling(BiFunction<Object, ChatContext, Object> onToolsCalling) {
            listenerAggregator.onToolsCalling = onToolsCalling;
            return this;
        }

        /**
         * Token 统计信息的回调方法
         *
         * @param onUsage Token 统计信息的回调函数
         * @see ChunkCallbackTransformer#onUsage(Object, ChatContext)
         */
        public Builder onUsage(BiFunction<Object, ChatContext, Object> onUsage) {
            listenerAggregator.onUsage = onUsage;
            return this;
        }

        /**
         * 基础信息/搜索结果的回调方法
         *
         * @param onGrounding 基础信息/搜索结果的回调函数
         * @see ChunkCallbackTransformer#onGrounding(Object, ChatContext)
         */
        public Builder onGrounding(BiFunction<Object, ChatContext, Object> onGrounding) {
            listenerAggregator.onGrounding = onGrounding;
            return this;
        }

        /**
         * 请求完成时的回调方法
         *
         * @param onCompletion 请求完成时的回调函数
         * @see ResultHandler#onCompletion(ChatResponse, ChatContext)
         */
        public Builder onCompletion(BiFunction<ChatResponse, ChatContext, ChatResponse> onCompletion) {
            listenerAggregator.onCompletion = onCompletion;
            return this;
        }

        /**
         * 请求发生错误时的回调方法
         *
         * @param onError 请求发生错误时的回调函数
         * @see ResultHandler#onError(ChatResponse, ChatContext)
         */
        public Builder onError(BiFunction<ChatResponse, ChatContext, ChatResponse> onError) {
            listenerAggregator.onError = onError;
            return this;
        }

        /**
         * 最终结果处理的回调方法。无论是否发生错误均会调用此方法。
         *
         * @param onFinal 请求最终结果的回调函数
         * @see ResultHandler#onFinal(ChatResponse, ChatContext)
         */
        public Builder onFinal(BiFunction<ChatResponse, ChatContext, ChatResponse> onFinal) {
            listenerAggregator.onFinal = onFinal;
            return this;
        }

        /**
         * 构建 ChatRequest 对象
         *
         * @return ChatRequest 实例
         */
        public ChatRequest build() {
            if (Objects.isNull(messages) || messages.isEmpty()) {
                throw new IllegalArgumentException("Messages cannot be null or empty");
            }
            if (Objects.isNull(options)) {
                options = ChatOptions.DEFAULT;
            }
            this.transportListener = listenerAggregator.toTransportListener();
            this.resultHandler = listenerAggregator.toResultHandler();
            this.chunkCallbackTransformer = listenerAggregator.toChunkCallback();
            return new ChatRequest(this);
        }

        /**
         * 内部聚合类
         */
        private static class LlmListenerAggregator {
            Consumer<ChatContext> onStart = context -> {};
            Consumer<ChatContext> onClose = context -> {};
            BiFunction<String, ChatContext, String> onText = (content, context) -> content;
            BiFunction<String, ChatContext, String> onThinking = (content, context) -> content;
            BiFunction<Object, ChatContext, Object> onToolsCalling = (content, context) -> content;
            BiFunction<Object, ChatContext, Object> onUsage = (content, context) -> content;
            BiFunction<Object, ChatContext, Object> onGrounding = (content, context) -> content;
            BiFunction<ChatResponse, ChatContext, ChatResponse> onCompletion = (response, context) -> response;
            BiFunction<ChatResponse, ChatContext, ChatResponse> onError = (response, context) -> response;
            BiFunction<ChatResponse, ChatContext, ChatResponse> onFinal = (response, context) -> response;

            TransportListener toTransportListener() {
                Objects.requireNonNull(onStart, "onStart cannot be null");
                Objects.requireNonNull(onClose, "onClose cannot be null");
                return new TransportListener() {
                    @Override
                    public void onStart(ChatContext context) {
                        onStart.accept(context);
                    }

                    @Override
                    public void onClose(ChatContext context) {
                        onClose.accept(context);
                    }
                };
            }

            ResultHandler toResultHandler() {
                Objects.requireNonNull(onCompletion, "onCompletion cannot be null");
                Objects.requireNonNull(onError, "onError cannot be null");
                Objects.requireNonNull(onFinal, "onFinal cannot be null");
                return new ResultHandler() {
                    @Override
                    public ChatResponse onCompletion(ChatResponse response, ChatContext context) {
                        return onCompletion.apply(response, context);
                    }

                    @Override
                    public ChatResponse onError(ChatResponse response, ChatContext context) {
                        return onError.apply(response, context);
                    }

                    @Override
                    public ChatResponse onFinal(ChatResponse response, ChatContext context) {
                        return onFinal.apply(response, context);
                    }
                };
            }

            ChunkCallbackTransformer toChunkCallback() {
                Objects.requireNonNull(onText, "onText cannot be null");
                Objects.requireNonNull(onThinking, "onThinking cannot be null");
                Objects.requireNonNull(onToolsCalling, "onToolsCalling cannot be null");
                Objects.requireNonNull(onUsage, "onUsage cannot be null");
                Objects.requireNonNull(onGrounding, "onGrounding cannot be null");
                return new ChunkCallbackTransformer() {
                    @Override
                    public String onText(String content, ChatContext context) {
                        return onText.apply(content, context);
                    }

                    @Override
                    public String onThinking(String content, ChatContext context) {
                        return onThinking.apply(content, context);
                    }

                    @Override
                    public Object onToolsCalling(Object content, ChatContext context) {
                        return onToolsCalling.apply(content, context);
                    }

                    @Override
                    public Object onUsage(Object content, ChatContext context) {
                        return onUsage.apply(content, context);
                    }

                    @Override
                    public Object onGrounding(Object content, ChatContext context) {
                        return onGrounding.apply(content, context);
                    }
                };
            }
        }
    }
}
