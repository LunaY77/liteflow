package com.yomahub.liteflow.ai.interact.transport;

import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;

/**
 * 数据传输接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface Transport {

    void start(ChatConfig config, ChatRequest request, TransportListener listener);

    void close();
}
