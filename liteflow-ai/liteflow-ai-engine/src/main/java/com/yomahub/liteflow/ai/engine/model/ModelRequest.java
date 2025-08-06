package com.yomahub.liteflow.ai.engine.model;

import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.util.request.RequestBodyConvertible;

/**
 * 大模型请求
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ModelRequest extends RequestBodyConvertible {

    default ChatRequest toChatRequest() {
        return (ChatRequest) this;
    }
}
