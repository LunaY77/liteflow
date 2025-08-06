package com.yomahub.liteflow.ai.context;

import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;

/**
 * 流式输出处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public interface StreamHandler {

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
     * @param content 工具调用内容，可能是工具调用的结果或相关信息
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onToolsCalling(Object content, InteractContext context);

    /**
     * 处理 Token 统计信息的回调方法。需要启用流式调用
     *
     * @param content Token 统计信息内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    TokenUsage onUsage(Object content, InteractContext context);

    /**
     * 处理基础信息/搜索结果的回调方法。需要启用流式调用
     *
     * @param content 基础信息或搜索结果内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onGrounding(Object content, InteractContext context);

    /**
     * 消息处理完成的回调方法。
     *
     * @param response 处理后的聊天响应结果
     * @param context  聊天上下文，包含处理过程中的状态和信息
     * @return 处理后的结果
     */
    ChatResponse onCompletion(ChatResponse response, InteractContext context);

    /**
     * 最终结果处理的回调方法。无论是否发生错误均会调用此方法。
     *
     * @param response 最终的聊天响应结果
     * @param context  聊天上下文，包含处理过程中的状态和信息
     * @return 处理后的结果
     */
    ChatResponse onFinal(ChatResponse response, InteractContext context);
}
