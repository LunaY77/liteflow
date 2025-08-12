package com.yomahub.liteflow.ai.engine.model.chat.entity;

import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.ModelRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.ai.engine.model.output.structure.parser.OutputParser;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
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
     * 流式输出模式，默认为 true
     */
    protected final boolean streaming;

    /**
     * 传输类型，默认为 SSE（Server-Sent Events）
     */
    protected final TransportType transportType;

    /**
     * 传输监听器，用于处理请求的开始和结束事件
     */
    protected final TransportListener transportListener;

    /**
     * 结果处理器，用于处理消息全部发送完毕后的结果
     */
    protected ResultHandler resultHandler;

    /**
     * 分块回调，用于处理消息分块的各种事件
     */
    protected final ChunkCallbackTransformer chunkCallbackTransformer;

    /**
     * 响应类型，默认为文本类型
     */
    protected final ResponseType responseType;

    /**
     * 输出解析器，用于解析模型的输出结果，由 targetType 生成
     */
    protected final OutputParser<?> outputParser;

    // ==== RequestBody 相关参数 =====
    protected static final String MESSAGES_KEY = "messages";
    protected static final String STREAM_KEY = "stream";
    // ==== RequestBody 相关参数 =====

    public ChatRequest() {
        this.messages = new ArrayList<>();
        this.options = ChatOptions.DEFAULT;
        this.streaming = true; // 默认启用流式输出
        this.transportType = TransportType.SSE; // 默认使用 SSE 传输
        this.transportListener = TransportListener.getDefault();
        this.resultHandler = ResultHandler.getDefault();
        this.chunkCallbackTransformer = ChunkCallbackTransformer.getDefault();
        this.responseType = ResponseType.TEXT; // 默认响应类型为文本
        TypeReference<String> targetType = new TypeReference<String>() {
        }; // 默认目标类型为 String
        this.outputParser = OutputParser.fromTypeReference(targetType);
    }

    public ChatRequest(
            List<Message> messages,
            ChatOptions options,
            boolean streaming,
            TransportType transportType,
            TransportListener transportListener,
            ResultHandler resultHandler,
            ChunkCallbackTransformer chunkCallbackTransformer,
            ResponseType responseType,
            TypeReference<?> targetType,
            boolean strict
    ) {
        this.messages = messages;
        this.options = options;
        this.streaming = streaming;
        this.transportType = transportType;
        this.transportListener = transportListener;
        this.resultHandler = resultHandler;
        this.chunkCallbackTransformer = chunkCallbackTransformer;
        this.responseType = responseType;
        this.outputParser = OutputParser.fromTypeReference(targetType, strict);
        checkTransportConsistency();
        checkResponseTypeConsistency();
    }

    /**
     * 私有构造函数，使用 Builder 模式创建 ChatRequest 实例
     *
     * @param builder Builder 实例
     */
    public ChatRequest(Builder<?> builder) {
        this.messages = builder.messages;
        this.options = builder.options;
        this.streaming = builder.streaming;
        this.transportType = builder.transportType;
        this.transportListener = builder.transportListener;
        this.resultHandler = builder.resultHandler;
        this.chunkCallbackTransformer = builder.chunkCallbackTransformer;
        this.responseType = builder.responseType;
        this.outputParser = OutputParser.fromTypeReference(builder.targetType, builder().strict);
        checkTransportConsistency();
        checkResponseTypeConsistency();
    }

    /**
     * 检查传输类型与流式输出模式的一致性。
     * 如果流式输出模式启用但传输类型为HTTP，则抛出异常。
     * 如果阻塞式输出模式启用但传输类型不是HTTP，则抛出异常。
     */
    protected void checkTransportConsistency() {
        if (this.streaming && this.transportType == TransportType.HTTP) {
            throw new IllegalArgumentException("Streaming mode is enabled, but HTTP transport is not supported. Please use SSE or WebSocket transport.");
        } else if (!this.streaming && this.transportType != TransportType.HTTP) {
            throw new IllegalArgumentException("Blocking mode is enabled, but the transport type does not support HTTP. Please use HTTP transport.");
        }
    }

    /**
     * 检查响应类型与目标类型的一致性。
     * 如果响应类型为文本（TEXT），但目标类型不是 String，则抛出异常。
     */
    protected void checkResponseTypeConsistency() {
        if (this.responseType == ResponseType.TEXT &&
                !Objects.equals("java.lang.String", getTargetType().getTypeName())) {
            throw new IllegalArgumentException("Response type is TEXT, but target type is not String. Please check the targetType setting.");
        }
    }

    @Override
    public RequestBody toRequestBody() {
        return RequestBody.of()
                .putIfNotEmpty(MESSAGES_KEY, appendFormatInstructionsIfNeeded())
                // 默认流式，如果不需要流式输出，则设置为false
                .putIf(!streaming, STREAM_KEY, streaming)
                .merge(options.toRequestBody());
    }

    /**
     * 对于某些模型，如果需要附加结构化输出提示词，可以在此方法中实现。
     *
     * @return 添加了结构化输出提示词的上下文
     */
    protected List<Message> appendFormatInstructionsIfNeeded() {
        return this.messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ChatOptions getOptions() {
        return options;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public TransportType getTransportType() {
        return transportType;
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

    public ResponseType getResponseType() {
        return responseType;
    }

    public Type getTargetType() {
        return outputParser.getTargetType();
    }

    public OutputParser<?> getOutputParser() {
        return outputParser;
    }

    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public static Builder<?> builder() {
        return new Builder.BuilderImpl();
    }

    public static abstract class Builder<B extends Builder<B>> {
        protected List<Message> messages;
        protected ChatOptions options;
        protected boolean streaming = true; // 默认启用流式输出
        protected TransportType transportType = TransportType.SSE; // 默认使用 SSE 传输
        protected final LlmListenerAggregator listenerAggregator = new LlmListenerAggregator();
        protected TransportListener transportListener;
        protected ResultHandler resultHandler;
        protected ChunkCallbackTransformer chunkCallbackTransformer;
        protected ResponseType responseType = ResponseType.TEXT;
        protected TypeReference<?> targetType = new TypeReference<String>() {
        };
        protected boolean strict = true;

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
         * 设置是否启用流式输出
         *
         * @param streaming 是否启用流式输出
         */
        public B streaming(boolean streaming) {
            this.streaming = streaming;
            return self();
        }

        /**
         * 设置传输类型
         *
         * @param transportType 传输类型
         * @see TransportType
         */
        public B transportType(TransportType transportType) {
            this.transportType = transportType;
            return self();
        }

        /**
         * 请求开始时的回调方法
         *
         * @param onStart 请求开始时的回调函数
         * @see TransportListener#onStart(InteractContext)
         */
        public B onStart(Consumer<InteractContext> onStart) {
            listenerAggregator.onStart = onStart;
            return self();
        }

        /**
         * 请求结束时的回调方法
         *
         * @param onClose 请求结束时的回调函数
         * @see TransportListener#onClose(InteractContext)
         */
        public B onClose(Consumer<InteractContext> onClose) {
            listenerAggregator.onClose = onClose;
            return self();
        }

        /**
         * 请求发生错误时的回调方法
         *
         * @param onError 请求发生错误时的回调函数
         * @see TransportListener#onError(InteractContext, Throwable)
         */
        public B onError(BiConsumer<InteractContext, Throwable> onError) {
            listenerAggregator.onError = onError;
            return self();
        }

        /**
         * 文本消息的回调方法
         *
         * @param onText 文本消息的回调函数
         * @see ChunkCallbackTransformer#onText(String, InteractContext)
         */
        public B onText(BiFunction<String, InteractContext, String> onText) {
            listenerAggregator.onText = onText;
            return self();
        }

        /**
         * 思考消息的回调方法
         *
         * @param onThinking 思考消息的回调函数
         * @see ChunkCallbackTransformer#onThinking(String, InteractContext)
         */
        public B onThinking(BiFunction<String, InteractContext, String> onThinking) {
            listenerAggregator.onThinking = onThinking;
            return self();
        }

        /**
         * 工具调用消息的回调方法
         *
         * @param onToolsCalling 工具调用消息的回调函数
         * @see ChunkCallbackTransformer#onToolsCalling(Object, InteractContext)
         */
        public B onToolsCalling(BiFunction<Object, InteractContext, Object> onToolsCalling) {
            listenerAggregator.onToolsCalling = onToolsCalling;
            return self();
        }

        /**
         * Token 统计信息的回调方法
         *
         * @param onUsage Token 统计信息的回调函数
         * @see ChunkCallbackTransformer#onUsage(Object, InteractContext)
         */
        public B onUsage(BiFunction<Object, InteractContext, Object> onUsage) {
            listenerAggregator.onUsage = onUsage;
            return self();
        }

        /**
         * 基础信息/搜索结果的回调方法
         *
         * @param onGrounding 基础信息/搜索结果的回调函数
         * @see ChunkCallbackTransformer#onGrounding(Object, InteractContext)
         */
        public B onGrounding(BiFunction<Object, InteractContext, Object> onGrounding) {
            listenerAggregator.onGrounding = onGrounding;
            return self();
        }

        /**
         * 请求完成时的回调方法
         *
         * @param onCompletion 请求完成时的回调函数
         * @see ResultHandler#onCompletion(ChatResponse, InteractContext)
         */
        public B onCompletion(BiFunction<ChatResponse, InteractContext, ChatResponse> onCompletion) {
            listenerAggregator.onCompletion = onCompletion;
            return self();
        }

        /**
         * 最终结果处理的回调方法。无论是否发生错误均会调用此方法。
         *
         * @param onFinal 请求最终结果的回调函数
         * @see ResultHandler#onFinal(ChatResponse, InteractContext)
         */
        public B onFinal(BiFunction<ChatResponse, InteractContext, ChatResponse> onFinal) {
            listenerAggregator.onFinal = onFinal;
            return self();
        }

        /**
         * 设置响应类型
         *
         * @param responseType 响应类型
         * @see ResponseType
         */
        public B responseType(ResponseType responseType) {
            this.responseType = responseType;
            return self();
        }

        /**
         * 设置目标类型引用，用于指定响应体的具体类型
         *
         * @param targetType 目标类型引用
         * @see TypeReference
         */
        public B targetType(TypeReference<?> targetType) {
            this.targetType = targetType;
            return self();
        }

        /**
         * 是否为严格模式，默认开启
         *
         * @param strict 是否为严格模式
         * @see JsonSchemaGenerator#generate(Type, boolean)
         */
        public B strict(boolean strict) {
            this.strict = strict;
            return self();
        }

        /**
         * 内部聚合类
         */
        protected static class LlmListenerAggregator {
            Consumer<InteractContext> onStart = context -> {
            };
            Consumer<InteractContext> onClose = context -> {
            };
            BiConsumer<InteractContext, Throwable> onError = (context, t) -> {
                throw new LiteFlowAIEngineException(t.getMessage(), t);
            };
            BiFunction<String, InteractContext, String> onText = (content, context) -> content;
            BiFunction<String, InteractContext, String> onThinking = (content, context) -> content;
            BiFunction<Object, InteractContext, Object> onToolsCalling = (content, context) -> content;
            BiFunction<Object, InteractContext, Object> onUsage = (content, context) -> content;
            BiFunction<Object, InteractContext, Object> onGrounding = (content, context) -> content;
            BiFunction<ChatResponse, InteractContext, ChatResponse> onCompletion = (response, context) -> response;
            BiFunction<ChatResponse, InteractContext, ChatResponse> onFinal = (response, context) -> response;

            TransportListener toTransportListener() {
                Objects.requireNonNull(onStart, "onStart cannot be null");
                Objects.requireNonNull(onClose, "onClose cannot be null");
                Objects.requireNonNull(onError, "onError cannot be null");
                return new TransportListener() {
                    @Override
                    public void onStart(InteractContext context) {
                        onStart.accept(context);
                    }

                    @Override
                    public void onClose(InteractContext context) {
                        onClose.accept(context);
                    }

                    @Override
                    public void onError(InteractContext context, Throwable t) {
                        onError.accept(context, t);
                    }
                };
            }

            ResultHandler toResultHandler() {
                Objects.requireNonNull(onCompletion, "onCompletion cannot be null");
                Objects.requireNonNull(onFinal, "onFinal cannot be null");
                return new ResultHandler() {
                    @Override
                    public ChatResponse onCompletion(ChatResponse response, InteractContext context) {
                        return onCompletion.apply(response, context);
                    }

                    @Override
                    public ChatResponse onFinal(ChatResponse response, InteractContext context) {
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
                    public String onText(String content, InteractContext context) {
                        return onText.apply(content, context);
                    }

                    @Override
                    public String onThinking(String content, InteractContext context) {
                        return onThinking.apply(content, context);
                    }

                    @Override
                    public Object onToolsCalling(Object content, InteractContext context) {
                        return onToolsCalling.apply(content, context);
                    }

                    @Override
                    public Object onUsage(Object content, InteractContext context) {
                        return onUsage.apply(content, context);
                    }

                    @Override
                    public Object onGrounding(Object content, InteractContext context) {
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
