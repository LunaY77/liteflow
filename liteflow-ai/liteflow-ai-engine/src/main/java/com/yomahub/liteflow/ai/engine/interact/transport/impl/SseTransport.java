package com.yomahub.liteflow.ai.engine.interact.transport.impl;

import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Sse传输实现，基于Server-Sent Events的非阻塞式传输
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class SseTransport implements Transport {

    private static final EngineLog LOG = EngineLogManager.getLogger(SseTransport.class);

    private final AtomicReference<EventSource> eventSourceRef = new AtomicReference<>();
    private final AtomicReference<OkHttpClient> clientRef = new AtomicReference<>();
    private final AtomicBoolean isStopped = new AtomicBoolean(false);

    @Override
    public Flowable<String> startStreaming(ChatConfig config, ChatRequest request) {
        return Flowable.create(emitter -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(config.getConnectTimeout())
                    .readTimeout(config.getReadTimeout())
                    .build();

            this.clientRef.set(client);

            try {
                // 构建SSE请求
                Request sseRequest = buildSseRequest(config, request);

                // 创建响应式的 EventSourceListener
                EventSourceListener listener = new EventSourceListener() {
                    @Override
                    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                    }

                    @Override
                    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                        if (config.isLogResponse()) {
                            LOG.info("SSE Response Event - id: {}, type: {}, data: {}", id, type, data);
                        }

                        // 发送数据到 Flowable
                        try {
                            emitter.onNext(data);
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }

                    @Override
                    public void onClosed(@NotNull EventSource eventSource) {
                        try {
                            if (!emitter.isCancelled()) {
                                emitter.onComplete();
                            }
                        } catch (Exception e) {
                            LOG.warn("Error completing stream", e);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                        if (!emitter.isCancelled()) {
                            if (t != null) {
                                emitter.onError(t);
                            } else {
                                emitter.onError(new RuntimeException("SSE connection failed"));
                            }
                        }
                    }
                };

                // 创建 EventSource
                EventSource eventSource = EventSources.createFactory(client)
                        .newEventSource(sseRequest, listener);
                eventSourceRef.set(eventSource);

                // 设置取消订阅时的清理逻辑
                emitter.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        close();
                    }

                    @Override
                    public boolean isDisposed() {
                        return isStopped.get();
                    }
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public String startBlocking(ChatConfig config, ChatRequest request) {
        throw new UnsupportedOperationException("SSE传输不支持阻塞式调用，请使用HTTP传输或调用start方法");
    }

    @Override
    public void close() {
        if (this.isStopped.compareAndSet(false, true)) {
            EventSource eventSource = eventSourceRef.get();
            if (Objects.nonNull(eventSource)) {
                eventSource.cancel();
            }
            OkHttpClient client = clientRef.get();
            if (Objects.nonNull(client)) {
                client.dispatcher().executorService().shutdown();
                client.connectionPool().evictAll();
            }
        }
    }

    private Request buildSseRequest(ChatConfig config, ChatRequest request) {
        String requestBody = buildRequestBody(config, request);

        Map<String, String> requestHeader = buildRequestHeader(config);

        if (config.isLogRequest()) {
            LOG.info("====== SSE Request Start ======");
            LOG.info("URL: {}", config.resolveUrl());
            LOG.info("Headers: {}", requestHeader);
            LOG.info("Body: {}", requestBody);
            LOG.info("======= SSE Request End =======");
        }

        return new Request.Builder()
                .url(config.resolveUrl())
                .headers(Headers.of(requestHeader))
                .addHeader("Accept", "text/event-stream")
                .addHeader("Cache-Control", "no-cache")
                .post(okhttp3.RequestBody.create(requestBody, okhttp3.MediaType.parse("application/json; charset=utf-8")))
                .build();
    }
}
