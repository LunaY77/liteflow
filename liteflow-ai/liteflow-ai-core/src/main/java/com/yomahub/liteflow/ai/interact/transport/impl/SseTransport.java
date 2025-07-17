package com.yomahub.liteflow.ai.interact.transport.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.yomahub.liteflow.ai.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.interact.transport.Transport;
import com.yomahub.liteflow.ai.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

/**
 * Sse传输实现，基于Server-Sent Events的非阻塞式传输
 *
 * @author 苍镜月
 * @since TODO
 */

public class SseTransport extends EventSourceListener implements Transport {

    private ChunkProcessPipeline pipeline;
    private TransportListener listener;
    private OkHttpClient client;
    private EventSource eventSource;

    @Override
    public void start(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline, TransportListener listener) {
        this.pipeline = pipeline;
        this.listener = listener;

        try {
            // 构建SSE请求
            Request sseRequest = buildSseRequest(config, request);

            // 创建EventSource实例
            client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(config.getTimeout())
                    .readTimeout(config.getTimeout())
                    .writeTimeout(config.getTimeout())
                    .build();

            this.eventSource = EventSources.createFactory(client)
                    .newEventSource(sseRequest, this);

            listener.onStart(pipeline.getContext());
        } catch (Exception e) {
            onFailure(eventSource, e, null);
        }
    }

    @Override
    public ChatResponse startBlocking(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline) {
        throw new UnsupportedOperationException("SSE传输不支持阻塞式调用，请使用HTTP传输或调用start方法");
    }

    @Override
    public void close() {
        eventSource.cancel();
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        super.onOpen(eventSource, response);
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        super.onEvent(eventSource, id, type, data);
        try {
            pipeline.processStreaming(data);
        } catch (Exception e) {
            onFailure(eventSource, e, null);
        }
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        super.onClosed(eventSource);

        // 通知连接关闭
        listener.onClose(pipeline.getContext());
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        super.onFailure(eventSource, t, response);
        listener.onClose(pipeline.getContext());
    }

    private Request buildSseRequest(ChatConfig config, ChatRequest request) {
        String requestBody = buildRequestBody(config, request);

        return new Request.Builder()
                .url(config.resolveUrl())
                .addHeader("Accept", "text/event-stream")
                .addHeader("Cache-Control", "no-cache")
                .post(okhttp3.RequestBody.create(requestBody, okhttp3.MediaType.parse("application/json")))
                .build();
    }
}
