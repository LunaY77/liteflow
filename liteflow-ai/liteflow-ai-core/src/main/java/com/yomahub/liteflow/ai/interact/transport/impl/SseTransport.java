package com.yomahub.liteflow.ai.interact.transport.impl;

import com.yomahub.liteflow.ai.interact.transport.Transport;
import com.yomahub.liteflow.ai.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import okhttp3.sse.EventSourceListener;

/**
 * Sse传输实现，基于Server-Sent Events的非阻塞式传输
 *
 * @author 苍镜月
 * @since TODO
 */

public class SseTransport extends EventSourceListener implements Transport {

    @Override
    public void start(ChatConfig config, ChatRequest request, TransportListener listener) {

    }

    @Override
    public void close() {

    }

}
