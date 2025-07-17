package com.yomahub.liteflow.ai.interact.callbacks;

import com.yomahub.liteflow.ai.interact.pipeline.ChatContext;
import com.yomahub.liteflow.ai.interact.pipeline.ChunkTransformer;

/**
 * 流式消息处理管道的回调接口。根据块数据的类型进行具体回调
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ChunkCallbackTransformer extends ChunkTransformer {

    /**
     * 处理文本消息的回调方法。
     *
     * @param content 文本内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    String onText(String content, ChatContext context);

    /**
     * 处理思考消息的回调方法。
     *
     * @param content 思考内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    String onThinking(String content, ChatContext context);

    /**
     * 处理工具调用消息的回调方法。
     *
     * @param content 工具调用内容，可能是工具调用的结果或相关信息
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onToolsCalling(Object content, ChatContext context);

    /**
     * 处理 Token 统计信息的回调方法
     *
     * @param content Token 统计信息内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onUsage(Object content, ChatContext context);

    /**
     * 处理基础信息/搜索结果的回调方法
     *
     * @param content 基础信息或搜索结果内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onGrounding(Object content, ChatContext context);

    @Override
    default String getTransformerType() {
        return "ChunkCallback";
    }
}
