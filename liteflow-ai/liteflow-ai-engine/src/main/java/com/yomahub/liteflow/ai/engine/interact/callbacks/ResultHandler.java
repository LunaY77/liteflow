package com.yomahub.liteflow.ai.engine.interact.callbacks;

import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

/**
 * 对消息全部发送完毕并转换后的结果进行处理的接口。
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ResultHandler {

    static ResultHandler getDefault() {
        return new ResultHandler() {
            @Override
            public ChatResponse onCompletion(ChatResponse response, InteractContext context) {
                return response;
            }

            @Override
            public ChatResponse onError(ChatResponse response, InteractContext context, Exception e) {
                return response;
            }

            @Override
            public ChatResponse onFinal(ChatResponse response, InteractContext context) {
                return response;
            }
        };
    }

    /**
     * 消息处理完成的回调方法。
     *
     * @param response 处理后的聊天响应结果
     * @param context  聊天上下文，包含处理过程中的状态和信息
     * @return 处理后的结果
     */
    ChatResponse onCompletion(ChatResponse response, InteractContext context);

    /**
     * 处理过程中发生错误的回调方法。
     *
     * @param response 处理后的聊天响应结果，可能包含错误信息
     * @param context  聊天上下文，包含处理过程中的状态和信息
     * @param e
     * @return 处理后的结果
     */
    ChatResponse onError(ChatResponse response, InteractContext context, Exception e);

    /**
     * 最终结果处理的回调方法。无论是否发生错误均会调用此方法。
     *
     * @param response 最终的聊天响应结果
     * @param context  聊天上下文，包含处理过程中的状态和信息
     * @return 处理后的结果
     */
    ChatResponse onFinal(ChatResponse response, InteractContext context);

}
