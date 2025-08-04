package com.yomahub.liteflow.ai.engine.util;

import com.alibaba.fastjson2.JSON;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * OkHttp 工具类
 *
 * <p>该工具类提供同步、异步、流式调用 Http 请求的方法。
 * 同时实现了 {@link AutoCloseable} 接口，可以使用 try-with-resource 进行资源管理</p>
 *
 * @author 苍镜月
 * @since TODO
 */

public final class HttpUtil implements AutoCloseable {

    private static final EngineLog LOG = EngineLogManager.getLogger(HttpUtil.class);
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient syncClient;

    /**
     * 私有构造函数，使用 Builder 模式创建 HttpUtil 实例。
     *
     * @param builder 构建器实例，包含连接、读取、写入超时等配置
     */
    private HttpUtil(Builder builder) {
        this.syncClient = new OkHttpClient.Builder()
                .connectTimeout(builder.connectTimeout)
                .readTimeout(builder.readTimeout)
                .writeTimeout(builder.writeTimeout)
                .build();
    }

    // --- 公共请求方法 ---

    /**
     * 执行同步 GET 请求。
     *
     * @param url     目标 URL。
     * @param headers 请求的 HTTP 头 (Map)，可以为 null。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String get(String url, Map<String, String> headers) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        addHeaders(requestBuilder, headers);
        Request request = requestBuilder.get().build();
        return execute(request);
    }

    /**
     * 执行同步 GET 请求。
     *
     * @param url 目标 URL。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * 执行同步 DELETE 请求。
     *
     * @param url     目标 URL。
     * @param headers 请求的 HTTP 头 (Map)，可以为 null。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String delete(String url, Map<String, String> headers) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        addHeaders(requestBuilder, headers);
        Request request = requestBuilder.delete().build();
        return execute(request);
    }

    /**
     * 执行同步 DELETE 请求。
     *
     * @param url     目标 URL。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String delete(String url) throws IOException {
        return delete(url, null);
    }

    /**
     * 执行同步 POST 请求，请求体为 Object (将序列化为 JSON)。
     *
     * @param url     目标 URL。
     * @param payload 将被序列化为 JSON 的请求体对象。
     * @param headers 请求的 HTTP 头 (Map)，可以为 null。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String post(String url, Object payload, Map<String, String> headers) throws IOException {
        String jsonBody = JSON.toJSONString(payload);
        return post(url, jsonBody, headers);
    }

    /**
     * 执行同步 POST 请求，请求体为 Object (将序列化为 JSON)。
     *
     * @param url     目标 URL。
     * @param payload 将被序列化为 JSON 的请求体对象。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String post(String url, Object payload) throws IOException {
        return post(url, payload, null);
    }

    /**
     * 执行同步 POST 请求，请求体为 String。
     *
     * @param url      目标 URL。
     * @param jsonBody 作为字符串的 JSON 请求体。
     * @param headers  请求的 HTTP 头 (Map)，可以为 null。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String post(String url, String jsonBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON_MEDIA_TYPE);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        addHeaders(requestBuilder, headers);
        return execute(requestBuilder.build());
    }

    /**
     * 执行同步 POST 请求，请求体为 String。
     *
     * @param url      目标 URL。
     * @param jsonBody 作为字符串的 JSON 请求体。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String post(String url, String jsonBody) throws IOException {
        return post(url, jsonBody, null);
    }

    /**
     * 执行同步 PUT 请求，请求体为 Object (将序列化为 JSON)。
     *
     * @param url     目标 URL。
     * @param payload 将被序列化为 JSON 的请求体对象。
     * @param headers 请求的 HTTP 头 (Map)，可以为 null。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String put(String url, Object payload, Map<String, String> headers) throws IOException {
        String jsonBody = JSON.toJSONString(payload);
        return put(url, jsonBody, headers);
    }

    /**
     * 执行同步 PUT 请求，请求体为 Object (将序列化为 JSON)。
     *
     * @param url     目标 URL。
     * @param payload 将被序列化为 JSON 的请求体对象。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String put(String url, Object payload) throws IOException {
        return put(url, payload, null);
    }

    /**
     * 执行同步 PUT 请求，请求体为 String。
     *
     * @param url      目标 URL。
     * @param jsonBody 作为字符串的 JSON 请求体。
     * @param headers  请求的 HTTP 头 (Map)，可以为 null。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String put(String url, String jsonBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON_MEDIA_TYPE);
        Request.Builder requestBuilder = new Request.Builder().url(url).put(body);
        addHeaders(requestBuilder, headers);
        return execute(requestBuilder.build());
    }

    /**
     * 执行同步 PUT 请求，请求体为 String。
     *
     * @param url      目标 URL。
     * @param jsonBody 作为字符串的 JSON 请求体。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败。
     */
    public String put(String url, String jsonBody) throws IOException {
        return put(url, jsonBody, null);
    }

    /**
     * 执行 multipart/form-data 文件上传请求。
     *
     * @param url       目标 URL。
     * @param formParts 表单部分，key 为字段名，value 可以是 String 或 File 对象。
     * @param headers   请求的 HTTP 头 (Map)，可以为 null。
     * @return 作为字符串的响应体。
     * @throws IOException 如果请求失败或 formParts 包含不支持的类型。
     */
    public String postMultipart(String url, Map<String, Object> formParts, Map<String, String> headers) throws IOException {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (Map.Entry<String, Object> entry : formParts.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                multipartBodyBuilder.addFormDataPart(key, (String) value);
            } else if (value instanceof File) {
                File file = (File) value;
                RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
                multipartBodyBuilder.addFormDataPart(key, file.getName(), fileBody);
            } else {
                throw new IllegalArgumentException("Unsupported type in formParts: " + value.getClass().getName());
            }
        }

        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        addHeaders(requestBuilder, headers);

        return execute(requestBuilder.build());
    }

    /**
     * 执行异步 POST 请求。
     *
     * @param url      目标 URL。
     * @param payload  将被序列化为 JSON 的请求体对象。
     * @param headers  请求的 HTTP 头 (Map)，可以为 null。
     * @param callback 用于处理响应或失败的 OkHttp 回调。
     */
    public void postAsync(String url, Object payload, Map<String, String> headers, Callback callback) {
        String jsonBody = JSON.toJSONString(payload);
        RequestBody body = RequestBody.create(jsonBody, JSON_MEDIA_TYPE);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        addHeaders(requestBuilder, headers);
        syncClient.newCall(requestBuilder.build()).enqueue(callback);
    }

    /**
     * 关闭调度器的执行服务并从连接池中逐出所有连接。
     * 当使用 try-with-resources 语句时，此方法会自动被调用。
     */
    @Override
    public void close() {
        LOG.info("正在关闭 OkHttp 客户端。");
        // 关闭同步客户端
        if (syncClient != null) {
            syncClient.dispatcher().executorService().shutdown();
            syncClient.connectionPool().evictAll();
        }
        LOG.info("OkHttp 客户端已成功关闭。");
    }

    /**
     * 核心请求执行逻辑。
     */
    private String execute(Request request) throws IOException {
        try (Response response = syncClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "null";
                throw new IOException("HTTP 请求失败: " + response.code() + " " + response.message() + " - 响应体: " + errorBody);
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("成功请求的响应体为 null。");
            }
            return responseBody.string();
        }
    }

    /**
     * 将 Map 类型的 headers 添加到 Request.Builder。
     */
    private void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            builder.headers(Headers.of(headers));
        }
    }

    /**
     * 静态工厂方法，用于获取一个新的构建器实例。
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 用于创建 {@link HttpUtil} 的构建器。
     */
    public static class Builder {
        private Duration connectTimeout = Duration.ofSeconds(30);
        private Duration readTimeout = Duration.ofSeconds(60);
        private Duration writeTimeout = Duration.ofSeconds(30);

        public Builder connectTimeout(Duration duration) {
            this.connectTimeout = Objects.requireNonNull(duration, "connectTimeout 不能为空");
            return this;
        }

        public Builder readTimeout(Duration duration) {
            this.readTimeout = Objects.requireNonNull(duration, "readTimeout 不能为空");
            return this;
        }

        public Builder writeTimeout(Duration duration) {
            this.writeTimeout = Objects.requireNonNull(duration, "writeTimeout 不能为空");
            return this;
        }

        public HttpUtil build() {
            return new HttpUtil(this);
        }
    }
}
