package com.yomahub.liteflow.ai.model.chat.entity;

import com.yomahub.liteflow.ai.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.interact.pipeline.ChatContext;
import com.yomahub.liteflow.ai.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.model.ModelRequest;
import com.yomahub.liteflow.ai.model.RequestBody;
import com.yomahub.liteflow.ai.model.chat.message.Message;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
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
    protected final List<Message> messages;

    /**
     * 聊天选项配置
     */
    protected final ChatOptions options;

    /**
     * 传输监听器，用于处理请求的开始和结束事件
     */
    protected final TransportListener transportListener;

    /**
     * 结果处理器，用于处理消息全部发送完毕后的结果
     */
    protected final ResultHandler resultHandler;

    /**
     * 分块回调，用于处理消息分块的各种事件
     */
    protected final ChunkCallbackTransformer chunkCallbackTransformer;

    protected static final String MESSAGES_KEY = "messages";

    public ChatRequest() {
        this.messages = new ArrayList<>();
        this.options = ChatOptions.DEFAULT;
        this.transportListener = TransportListener.getDefault();
        this.resultHandler = ResultHandler.getDefault();
        this.chunkCallbackTransformer = ChunkCallbackTransformer.getDefault();
    }

    public ChatRequest(
            List<Message> messages,
            ChatOptions options,
            TransportListener transportListener,
            ResultHandler resultHandler,
            ChunkCallbackTransformer chunkCallbackTransformer
    ) {
        this.messages = messages;
        this.options = options;
        this.transportListener = transportListener;
        this.resultHandler = resultHandler;
        this.chunkCallbackTransformer = chunkCallbackTransformer;
    }

    /**
     * 私有构造函数，使用 Builder 模式创建 ChatRequest 实例
     *
     * @param builder Builder 实例
     */
    public ChatRequest(Builder<?> builder) {
        this.messages = builder.messages;
        this.options = builder.options;
        this.transportListener = builder.transportListener;
        this.resultHandler = builder.resultHandler;
        this.chunkCallbackTransformer = builder.chunkCallbackTransformer;
    }

    @Override
    public RequestBody toRequestBody() {
        return RequestBody.of()
                .putIfNotEmpty(MESSAGES_KEY, messages)
                .merge(options.toRequestBody());
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

    public ChunkCallbackTransformer getChunkCallbackTransformer() {
        return chunkCallbackTransformer;
    }

    public static Builder<?> builder() {
        return new Builder.BuilderImpl();
    }

    public static abstract class Builder<B extends Builder<B>> {
        protected List<Message> messages;
        protected ChatOptions options;
        protected final LlmListenerAggregator listenerAggregator = new LlmListenerAggregator();
        protected TransportListener transportListener;
        protected ResultHandler resultHandler;
        protected ChunkCallbackTransformer chunkCallbackTransformer;

        public abstract B self();

        public abstract ChatRequest build();

        /**
         * 检查并分配必要的属性值
         */
        protected void checkAndAssign() {
            if (Objects.isNull(options)) {
                options = ChatOptions.DEFAULT;
            }
            this.transportListener = listenerAggregator.toTransportListener();
            this.resultHandler = listenerAggregator.toResultHandler();
            this.chunkCallbackTransformer = listenerAggregator.toChunkCallbackTransformer();
        }

        /**
         * 设置上下文消息
         *
         * @param messages 上下文消息列表
         * @see Message
         */
        public B messages(List<Message> messages) {
            this.messages = messages;
            return self();
        }

        /**
         * 设置聊天选项
         *
         * @param options 聊天选项配置
         * @see ChatOptions
         */
        public B options(ChatOptions options) {
            this.options = options;
            return self();
        }

        /**
         * 请求开始时的回调方法
         *
         * @param onStart 请求开始时的回调函数
         * @see TransportListener#onStart(ChatContext)
         */
        public B onStart(Consumer<ChatContext> onStart) {
            listenerAggregator.onStart = onStart;
            return self();
        }

        /**
         * 请求结束时的回调方法
         *
         * @param onClose 请求结束时的回调函数
         * @see TransportListener#onClose(ChatContext)
         */
        public B onClose(Consumer<ChatContext> onClose) {
            listenerAggregator.onClose = onClose;
            return self();
        }

        /**
         * 文本消息的回调方法
         *
         * @param onText 文本消息的回调函数
         * @see ChunkCallbackTransformer#onText(String, ChatContext)
         */
        public B onText(BiFunction<String, ChatContext, String> onText) {
            listenerAggregator.onText = onText;
            return self();
        }

        /**
         * 思考消息的回调方法
         *
         * @param onThinking 思考消息的回调函数
         * @see ChunkCallbackTransformer#onThinking(String, ChatContext)
         */
        public B onThinking(BiFunction<String, ChatContext, String> onThinking) {
            listenerAggregator.onThinking = onThinking;
            return self();
        }

        /**
         * 工具调用消息的回调方法
         *
         * @param onToolsCalling 工具调用消息的回调函数
         * @see ChunkCallbackTransformer#onToolsCalling(Object, ChatContext)
         */
        public B onToolsCalling(BiFunction<Object, ChatContext, Object> onToolsCalling) {
            listenerAggregator.onToolsCalling = onToolsCalling;
            return self();
        }

        /**
         * Token 统计信息的回调方法
         *
         * @param onUsage Token 统计信息的回调函数
         * @see ChunkCallbackTransformer#onUsage(Object, ChatContext)
         */
        public B onUsage(BiFunction<Object, ChatContext, Object> onUsage) {
            listenerAggregator.onUsage = onUsage;
            return self();
        }

        /**
         * 基础信息/搜索结果的回调方法
         *
         * @param onGrounding 基础信息/搜索结果的回调函数
         * @see ChunkCallbackTransformer#onGrounding(Object, ChatContext)
         */
        public B onGrounding(BiFunction<Object, ChatContext, Object> onGrounding) {
            listenerAggregator.onGrounding = onGrounding;
            return self();
        }

        /**
         * 请求完成时的回调方法
         *
         * @param onCompletion 请求完成时的回调函数
         * @see ResultHandler#onCompletion(ChatResponse, ChatContext)
         */
        public B onCompletion(BiFunction<ChatResponse, ChatContext, ChatResponse> onCompletion) {
            listenerAggregator.onCompletion = onCompletion;
            return self();
        }

        /**
         * 请求发生错误时的回调方法
         *
         * @param onError 请求发生错误时的回调函数
         * @see ResultHandler#onError(ChatResponse, ChatContext, Exception)
         */
        public B onError(TriFunction<ChatResponse, ChatContext, Exception, ChatResponse> onError) {
            listenerAggregator.onError = onError;
            return self();
        }

        /**
         * 最终结果处理的回调方法。无论是否发生错误均会调用此方法。
         *
         * @param onFinal 请求最终结果的回调函数
         * @see ResultHandler#onFinal(ChatResponse, ChatContext)
         */
        public B onFinal(BiFunction<ChatResponse, ChatContext, ChatResponse> onFinal) {
            listenerAggregator.onFinal = onFinal;
            return self();
        }

        /**
         * 内部聚合类
         */
        protected static class LlmListenerAggregator {
            Consumer<ChatContext> onStart = context -> {};
            Consumer<ChatContext> onClose = context -> {};
            BiFunction<String, ChatContext, String> onText = (content, context) -> content;
            BiFunction<String, ChatContext, String> onThinking = (content, context) -> content;
            BiFunction<Object, ChatContext, Object> onToolsCalling = (content, context) -> content;
            BiFunction<Object, ChatContext, Object> onUsage = (content, context) -> content;
            BiFunction<Object, ChatContext, Object> onGrounding = (content, context) -> content;
            BiFunction<ChatResponse, ChatContext, ChatResponse> onCompletion = (response, context) -> response;
            TriFunction<ChatResponse, ChatContext, Exception, ChatResponse> onError = (response, context, e) -> {
                e.printStackTrace();
                return response;
            };
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
                    public ChatResponse onError(ChatResponse response, ChatContext context, Exception e) {
                        return onError.apply(response, context, e);
                    }

                    @Override
                    public ChatResponse onFinal(ChatResponse response, ChatContext context) {
                        return onFinal.apply(response, context);
                    }
                };
            }

            ChunkCallbackTransformer toChunkCallbackTransformer() {
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

        private static class BuilderImpl extends Builder<BuilderImpl> {

            @Override
            public BuilderImpl self() {
                return this;
            }

            @Override
            public ChatRequest build() {
                checkAndAssign();
                return new ChatRequest(this);
            }
        }
    }
}
