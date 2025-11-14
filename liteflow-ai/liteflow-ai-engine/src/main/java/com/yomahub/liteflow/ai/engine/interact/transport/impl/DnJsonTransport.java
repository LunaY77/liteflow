package com.yomahub.liteflow.ai.engine.interact.transport.impl;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 一个用于处理"换行符分隔的 JSON"(Delimiter-Newline JSON)流的 LLM 客户端。(Ollama流式输出应使用该传输方式)
 * <p>
 * 这个客户端适用于一些非标准的流式 API，这些 API 在 HTTP 响应体中直接返回一连串由换行符分隔的 JSON 对象，
 * 而不是使用标准的 Server-Sent Events (SSE) 格式。
 * <p>
 * 它的工作方式是：
 * 1. 发起一个异步的 HTTP POST 请求。
 * 2.在 onResponse 回调中，逐行读取响应体。
 * 3. 尝试将每一行解析为一个 JSON 对象，并将其作为一条消息传递给 listener。
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class DnJsonTransport implements Transport {

    private static final EngineLog LOG = EngineLogManager.getLogger(DnJsonTransport.class);

    private final AtomicReference<OkHttpClient> clientRef = new AtomicReference<>();
    private final AtomicBoolean isStop = new AtomicBoolean(false);

    @Override
    public Flowable<String> startStreaming(ChatConfig config, ChatRequest request) {
        return Flowable.create(emitter -> {
            Request dnJsonRequest = buildDnJsonRequest(config, request);

            OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(config.getConnectTimeout())
                    .readTimeout(config.getReadTimeout())
                    .build();

            this.clientRef.set(client);

            client.newCall(dnJsonRequest).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            emitter.onError(new LiteFlowAIEngineException("error response when calling LLM: " + response.message()));
                            return;
                        }
                        ResponseBody body = response.body();
                        if (Objects.isNull(body)) {
                            emitter.onError(new LiteFlowAIEngineException("response body is null when calling LLM"));
                            return;
                        }

                        // 逐行读取响应体，响应体为换行符分隔的 JSON
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(body.byteStream()))) {
                            String line = br.readLine();

                            while (StrUtil.isNotBlank(line)) {
                                if (config.isLogResponse()) {
                                    LOG.info("DN-JSON Response: {}", line);
                                }

                                try {
                                    emitter.onNext(line);
                                } catch (Exception e) {
                                    emitter.onError(e);
                                    return;
                                }

                                line = br.readLine();
                            }

                            emitter.onComplete();
                        }
                    } finally {
                        close();
                    }
                }
            });

            // 设置取消订阅时的清理逻辑
            emitter.setDisposable(new Disposable() {
                @Override
                public void dispose() {
                    close();
                }

                @Override
                public boolean isDisposed() {
                    return isStop.get();
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public String startBlocking(ChatConfig config, ChatRequest request) {
        throw new UnsupportedOperationException("DN-JSON传输不支持阻塞式调用，请使用HTTP传输");
    }

    @Override
    public void close() {
        if (this.isStop.compareAndSet(false, true)) {
            OkHttpClient client = clientRef.get();
            if (Objects.nonNull(client)) {
                client.dispatcher().executorService().shutdown();
                client.connectionPool().evictAll();
            }
        }
    }

    private Request buildDnJsonRequest(ChatConfig config, ChatRequest request) {
        String requestBody = buildRequestBody(config, request);

        Map<String, String> requestHeader = buildRequestHeader(config);

        if (config.isLogRequest()) {
            LOG.info("====== DN-JSON Request Start ======");
            LOG.info("URL: {}", config.resolveUrl());
            LOG.info("Headers: {}", requestHeader);
            LOG.info("Body: {}", requestBody);
            LOG.info("======= DN-JSON Request End =======");
        }

        return new Request.Builder()
                .url(config.resolveUrl())
                .headers(Headers.of(requestHeader))
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build();
    }
}
