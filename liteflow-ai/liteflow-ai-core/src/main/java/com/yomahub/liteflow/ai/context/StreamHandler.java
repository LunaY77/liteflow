package com.yomahub.liteflow.ai.context;

import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * 流式输出处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface StreamHandler {

    static Builder builder() {
        return new Builder();
    }

    /**
     * 请求开始时的回调方法
     *
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    void onStart(InteractContext context);

    /**
     * 请求关闭时的回调方法
     *
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    void onClose(InteractContext context);

    /**
     * 处理过程中发生错误的回调方法。
     *
     * @param context 聊天上下文，包含处理过程中的状态和信息
     * @param t
     */
    void onError(InteractContext context, Throwable t);

    /**
     * 处理文本消息的回调方法。需要启用流式调用
     *
     * @param content 文本内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    String onText(String content, InteractContext context);

    /**
     * 处理思考消息的回调方法。需要启用流式调用
     *
     * @param content 思考内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    String onThinking(String content, InteractContext context);

    /**
     * 处理工具调用消息的回调方法。需要启用流式调用
     *
     * @param toolCalls 工具调用内容，可能是工具调用的结果或相关信息
     * @param context   聊天上下文，包含处理过程中的状态和信息
     */
    List<ToolCall> onToolsCalling(List<ToolCall> toolCalls, InteractContext context);

    /**
     * 处理 Token 统计信息的回调方法。需要启用流式调用
     *
     * @param content Token 统计信息内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    Object onUsage(Object content, InteractContext context);

    /**
     * 处理基础信息/搜索结果的回调方法。需要启用流式调用
     *
     * @param content 基础信息或搜索结果内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onGrounding(Object content, InteractContext context);

    /**
     * 消息处理完成的回调方法。(流式传输/阻塞式传输均会调用)
     *
     * @param response 处理后的聊天响应结果
     * @param context  聊天上下文，包含处理过程中的状态和信息
     * @return 处理后的结果
     */
    ChatResponse onCompletion(ChatResponse response, InteractContext context);

    /**
     * 最终结果处理的回调方法。无论是否发生错误均会调用此方法。(流式传输/阻塞式传输均会调用)
     *
     * @param response 最终的聊天响应结果
     * @param context  聊天上下文，包含处理过程中的状态和信息
     * @return 处理后的结果
     */
    ChatResponse onFinal(ChatResponse response, InteractContext context);

    class Builder {
        Consumer<InteractContext> onStart = context -> {
        };
        Consumer<InteractContext> onClose = context -> {
        };
        BiConsumer<InteractContext, Throwable> onError = (context, t) -> {
            throw new LiteFlowAIEngineException(t.getMessage(), t);
        };
        BiFunction<String, InteractContext, String> onText = (content, context) -> content;
        BiFunction<String, InteractContext, String> onThinking = (content, context) -> content;
        BiFunction<List<ToolCall>, InteractContext, List<ToolCall>> onToolsCalling = (toolCalls, context) -> toolCalls;
        BiFunction<Object, InteractContext, Object> onUsage = (content, context) -> content;
        BiFunction<Object, InteractContext, Object> onGrounding = (content, context) -> content;
        BiFunction<ChatResponse, InteractContext, ChatResponse> onCompletion = (response, context) -> response;
        BiFunction<ChatResponse, InteractContext, ChatResponse> onFinal = (response, context) -> response;

        /**
         * 请求开始时的回调方法
         *
         * @param onStart 请求开始时的回调函数
         * @see TransportListener#onStart(InteractContext)
         */
        public Builder onStart(Consumer<InteractContext> onStart) {
            this.onStart = onStart;
            return this;
        }

        /**
         * 请求结束时的回调方法
         *
         * @param onClose 请求结束时的回调函数
         * @see TransportListener#onClose(InteractContext)
         */
        public Builder onClose(Consumer<InteractContext> onClose) {
            this.onClose = onClose;
            return this;
        }

        /**
         * 请求发生错误时的回调方法
         *
         * @param onError 请求发生错误时的回调函数
         * @see TransportListener#onError(InteractContext, Throwable)
         */
        public Builder onError(BiConsumer<InteractContext, Throwable> onError) {
            this.onError = onError;
            return this;
        }

        /**
         * 文本消息的回调方法
         *
         * @param onText 文本消息的回调函数
         * @see ChunkCallbackTransformer#onText(String, InteractContext)
         */
        public Builder onText(BiFunction<String, InteractContext, String> onText) {
            this.onText = onText;
            return this;
        }

        /**
         * 思考消息的回调方法
         *
         * @param onThinking 思考消息的回调函数
         * @see ChunkCallbackTransformer#onThinking(String, InteractContext)
         */
        public Builder onThinking(BiFunction<String, InteractContext, String> onThinking) {
            this.onThinking = onThinking;
            return this;
        }

        /**
         * 工具调用消息的回调方法
         *
         * @param onToolsCalling 工具调用消息的回调函数
         * @see ChunkCallbackTransformer#onToolsCalling(List, InteractContext)
         */
        public Builder onToolsCalling(BiFunction<List<ToolCall>, InteractContext, List<ToolCall>> onToolsCalling) {
            this.onToolsCalling = onToolsCalling;
            return this;
        }

        /**
         * Token 统计信息的回调方法
         *
         * @param onUsage Token 统计信息的回调函数
         * @see ChunkCallbackTransformer#onUsage(Object, InteractContext)
         */
        public Builder onUsage(BiFunction<Object, InteractContext, Object> onUsage) {
            this.onUsage = onUsage;
            return this;
        }

        /**
         * 基础信息/搜索结果的回调方法
         *
         * @param onGrounding 基础信息/搜索结果的回调函数
         * @see ChunkCallbackTransformer#onGrounding(Object, InteractContext)
         */
        public Builder onGrounding(BiFunction<Object, InteractContext, Object> onGrounding) {
            this.onGrounding = onGrounding;
            return this;
        }

        /**
         * 请求完成时的回调方法(流式传输/阻塞式传输均会调用)
         *
         * @param onCompletion 请求完成时的回调函数
         * @see ResultHandler#onCompletion(ChatResponse, InteractContext)
         */
        public Builder onCompletion(BiFunction<ChatResponse, InteractContext, ChatResponse> onCompletion) {
            this.onCompletion = onCompletion;
            return this;
        }

        /**
         * 最终结果处理的回调方法。无论是否发生错误均会调用此方法。(流式传输/阻塞式传输均会调用)
         *
         * @param onFinal 请求最终结果的回调函数
         * @see ResultHandler#onFinal(ChatResponse, InteractContext)
         */
        public Builder onFinal(BiFunction<ChatResponse, InteractContext, ChatResponse> onFinal) {
            this.onFinal = onFinal;
            return this;
        }

        public StreamHandler build() {
            return new StreamHandler() {
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

                @Override
                public String onText(String content, InteractContext context) {
                    return onText.apply(content, context);
                }

                @Override
                public String onThinking(String content, InteractContext context) {
                    return onThinking.apply(content, context);
                }

                @Override
                public List<ToolCall> onToolsCalling(List<ToolCall> toolCalls, InteractContext context) {
                    return onToolsCalling.apply(toolCalls, context);
                }

                @Override
                public Object onUsage(Object content, InteractContext context) {
                    return onUsage.apply(content, context);
                }

                @Override
                public Object onGrounding(Object content, InteractContext context) {
                    return onGrounding.apply(content, context);
                }

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
    }
}
