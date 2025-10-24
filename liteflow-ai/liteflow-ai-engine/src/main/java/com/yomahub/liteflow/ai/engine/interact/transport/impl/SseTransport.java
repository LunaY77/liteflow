package com.yomahub.liteflow.ai.engine.interact.transport.impl;

import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * Sse传输实现，基于Server-Sent Events的非阻塞式传输
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class SseTransport extends EventSourceListener implements Transport {

    private ChunkProcessPipeline pipeline;
    private TransportListener listener;
    private OkHttpClient client;
    private EventSource eventSource;
    private boolean isStop = false;

    @Override
    public void start(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline, TransportListener listener) {
        this.pipeline = pipeline;
        this.listener = listener;

        try {
            // 构建SSE请求
            Request sseRequest = buildSseRequest(config, request);

            // 创建EventSource实例
            client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(config.getConnectTimeout())
                    .readTimeout(config.getReadTimeout())
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
        if (!this.isStop) {
            try {
                this.isStop = true;
                this.listener.onClose(pipeline.getContext());
            } finally {
                if (Objects.nonNull(eventSource)) {
                    eventSource.cancel();
                }
                if (Objects.nonNull(client)) {
                    client.dispatcher().executorService().shutdown();
                    client.connectionPool().evictAll();
                }
            }
        }
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        super.onOpen(eventSource, response);
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        super.onEvent(eventSource, id, type, data);
        // 如果返回 true 表示流式响应结束，关闭连接, 有一些模型不会主动关闭连接，需要在这里判断
        if (pipeline.processStreaming(data)) {
            close();
        }
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        super.onClosed(eventSource);
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        super.onFailure(eventSource, t, response);
        close();
        this.listener.onError(pipeline.getContext(), t);
    }

    private Request buildSseRequest(ChatConfig config, ChatRequest request) {
        String requestBody = buildRequestBody(config, request);

        System.out.println("====== HTTP Request Start ======");
        System.out.println(requestBody);
        System.out.println("======= HTTP Request End =======");

        Map<String, String> requestHeader = buildRequestHeader(config);

        return new Request.Builder()
                .url(config.resolveUrl())
                .headers(Headers.of(requestHeader))
                .addHeader("Accept", "text/event-stream")
                .addHeader("Cache-Control", "no-cache")
                .post(okhttp3.RequestBody.create(requestBody, okhttp3.MediaType.parse("application/json; charset=utf-8")))
                .build();
    }
}
