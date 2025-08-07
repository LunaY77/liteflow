package com.yomahub.liteflow.ai.engine.interact.transport.impl;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

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
 * @since TODO
 */

public class DnJsonTransport implements Transport, Callback {

    private ChunkProcessPipeline pipeline;
    private TransportListener listener;
    private OkHttpClient client;
    private boolean isStop = false;

    @Override
    public void start(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline, TransportListener listener) {
        this.pipeline = pipeline;
        this.listener = listener;

        Request dnJsonRequest = buildDnJsonRequest(config, request);

        client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout())
                .readTimeout(config.getReadTimeout())
                .build();

        this.listener.onStart(pipeline.getContext());
        // 异步执行请求，this 作为回调处理器
        this.client.newCall(dnJsonRequest).enqueue(this);
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
                if (Objects.nonNull(client)) {
                    client.dispatcher().executorService().shutdown();
                    client.connectionPool().evictAll();
                }
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        this.listener.onError(pipeline.getContext(), e);
        close();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!response.isSuccessful()) {
            this.listener.onError(pipeline.getContext(), new LiteFlowAIEngineException("error response when calling LLM: " + response.message()));
            close();
            return;
        }
        ResponseBody body = response.body();
        if (Objects.isNull(body)) {
            this.listener.onError(pipeline.getContext(), new LiteFlowAIEngineException("response body is null when calling LLM"));
            close();
            return;
        }

        // 逐行读取响应体，响应体为换行符分隔的 JSON
        try (BufferedReader br = new BufferedReader(new InputStreamReader(body.byteStream()))) {
            String line = br.readLine();
            while (StrUtil.isNotBlank(line)) {
                pipeline.processStreaming(line);
                line = br.readLine();
            }
        } finally {
            // 确保在读取完毕后关闭资源
            close();
        }
    }

    private Request buildDnJsonRequest(ChatConfig config, ChatRequest request) {
        String requestBody = buildRequestBody(config, request);
        Map<String, String> requestHeader = buildRequestHeader(config);

        return new Request.Builder()
                .url(config.resolveUrl())
                .headers(Headers.of(requestHeader))
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build();
    }
}
