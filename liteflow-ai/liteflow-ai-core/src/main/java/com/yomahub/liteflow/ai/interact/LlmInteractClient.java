package com.yomahub.liteflow.ai.interact;

import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;

/**
 * 大模型交互客户端，统筹消息传输、协议转换等功能。
 *
 * @author 苍镜月
 * @since TODO
 */

public class LlmInteractClient implements InteractClient {

    private ChatConfig chatConfig;

    public LlmInteractClient(ChatConfig chatConfig) {
        this.chatConfig = chatConfig;
    }

    @Override
    public void stream(ChatConfig config, ChatRequest request) {

    }

    @Override
    public void chat(ChatConfig config, ChatRequest request) {

    }
}
