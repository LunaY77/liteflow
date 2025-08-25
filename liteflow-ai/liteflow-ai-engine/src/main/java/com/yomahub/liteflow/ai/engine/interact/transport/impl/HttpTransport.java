package com.yomahub.liteflow.ai.engine.interact.transport.impl;

import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.util.HttpUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Http传输实现，阻塞式传输
 *
 * @author 苍镜月
 * @since TODO
 */

public class HttpTransport implements Transport {

    @Override
    public void start(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline, TransportListener listener) {
        throw new UnsupportedOperationException("HTTP传输不支持流式调用，请使用SSE传输或调用startBlocking方法");
    }

    @Override
    public ChatResponse startBlocking(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline) {
        try (HttpUtil httpUtil = HttpUtil
                .builder()
                .connectTimeout(config.getConnectTimeout())
                .readTimeout(config.getReadTimeout())
                .build()) {
            // 构建请求体
            String requestBody = buildRequestBody(config, request);
            // 构建请求头
            Map<String, String> requestHeader = buildRequestHeader(config);

            // 发送HTTP请求
            String responseBody = httpUtil.post(config.resolveUrl(), requestBody, requestHeader);

            // 处理响应
            return pipeline.processBlocking(responseBody);
        } catch (IOException e) {
            throw new LiteFlowAIEngineException("阻塞调用大模型失败", e);
        }
    }

    @Override
    public void close() {
        // 空实现
        // HTTP 连接在 try-with-resource 中自动关闭
    }
}
