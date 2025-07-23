package com.yomahub.liteflow.ai.context;

import dev.langchain4j.model.chat.response.ChatResponse;

/**
 * 流式输出处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public interface StreamHandler {

    void onPartialResponse(String partialResponse);

    void onCompleteResponse(ChatResponse completeResponse);

    void onError(Throwable error);
}
