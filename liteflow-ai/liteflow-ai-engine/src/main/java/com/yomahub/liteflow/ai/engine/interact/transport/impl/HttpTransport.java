package com.yomahub.liteflow.ai.engine.interact.transport.impl;

import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.util.HttpUtil;
import io.reactivex.rxjava3.core.Flowable;

import java.io.IOException;
import java.util.Map;

/**
 * Http传输实现，阻塞式传输
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class HttpTransport implements Transport {

    private static final EngineLog LOG = EngineLogManager.getLogger(HttpTransport.class);

    @Override
    public Flowable<String> startStreaming(ChatConfig config, ChatRequest request) {
        throw new UnsupportedOperationException("HTTP传输不支持流式调用，请使用SSE传输或调用startBlocking方法");
    }

    @Override
    public String startBlocking(ChatConfig config, ChatRequest request) {
        try (HttpUtil httpUtil = HttpUtil
                .builder()
                .connectTimeout(config.getConnectTimeout())
                .readTimeout(config.getReadTimeout())
                .build()) {
            // 构建请求体
            String requestBody = buildRequestBody(config, request);
            // 构建请求头
            Map<String, String> requestHeader = buildRequestHeader(config);

            // 记录请求日志
            if (config.isLogRequest()) {
                LOG.info("====== HTTP Request Start ======");
                LOG.info("URL: {}", config.resolveUrl());
                LOG.info("Headers: {}", requestHeader);
                LOG.info("Body: {}", requestBody);
                LOG.info("======= HTTP Request End =======");
            }

            // 发送HTTP请求
            String responseBody = httpUtil.post(config.resolveUrl(), requestBody, requestHeader);

            // 记录响应日志
            if (config.isLogResponse()) {
                LOG.info("====== HTTP Response Start ======");
                LOG.info("response: {}", responseBody);
                LOG.info("======= HTTP Response End =======");
            }

            // 返回响应体
            return responseBody;
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
