package com.yomahub.liteflow.ai.interact;

import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;

/**
 * 交互客户端接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface InteractClient {

    void stream(ChatConfig config, ChatRequest request);

    void chat(ChatConfig config, ChatRequest request);
}
