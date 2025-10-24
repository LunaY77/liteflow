package com.yomahub.liteflow.test.ai.mock;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 测试数据读取工具类
 * 用于读取测试资源文件中的模型响应数据
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class TestDataReader {

    private static final String RESOURCE_BASE_PATH = "/mock/data/";

    /**
     * 请求类型枚举
     * <p>
     * 该枚举直接映射到 shell 脚本中的 <request_type> 参数。
     * 命名规则 (output_{provider}_{request_name}.log) 基于此统一。
     */
    public enum RequestType {
        // 文本
        STREAMING_TEXT("streaming_text", true),
        BLOCKING_TEXT("blocking_text", false),

        // 工具调用
        STREAMING_TOOL_CALL("streaming_tool_call", true),
        BLOCKING_TOOL_CALL("blocking_tool_call", false),
        STREAMING_TOOL_CALL_2("streaming_tool_call_2", true),
        BLOCKING_TOOL_CALL_2("blocking_tool_call_2", false),

        // 结构化 (JSON Schema)
        STREAMING_STRUCTURED("streaming_structured", true),
        BLOCKING_STRUCTURED("blocking_structured", false),

        // 分类
        CLASSIFY("classify", false),
        CLASSIFY_MULTI("classify_multi", false);

        private final String requestName;
        private final boolean isStreaming;

        RequestType(String requestName, boolean isStreaming) {
            this.requestName = requestName;
            this.isStreaming = isStreaming;
        }

        public String getRequestName() {
            return requestName;
        }

        public boolean isStreaming() {
            return isStreaming;
        }
    }

    /**
     * 通用方法：获取流式响应数据
     *
     * @param provider    提供者
     * @param requestType 请求类型 (必须是流式类型)
     * @return 流式响应chunk列表
     * @throws IllegalArgumentException 如果传入的 RequestType 不是流式类型
     */
    public static List<String> getStreamingChunks(ProviderEnum provider, RequestType requestType) {
        if (!requestType.isStreaming()) {
            return Collections.emptyList();
        }
        String fileName = buildFileName(provider, requestType);
        return readStreamingLogFile(provider, fileName);
    }

    /**
     * 通用方法：获取阻塞式响应数据
     *
     * @param provider    提供者
     * @param requestType 请求类型 (必须是阻塞式类型)
     * @return 阻塞式响应JSON字符串
     * @throws IllegalArgumentException 如果传入的 RequestType 是流式类型
     */
    public static String getBlockingResponse(ProviderEnum provider, RequestType requestType) {
        if (requestType.isStreaming()) {
            return "";
        }
        String fileName = buildFileName(provider, requestType);
        return readBlockingLogFile(provider, fileName);
    }

    /**
     * 根据提供者和请求类型构建标准文件名
     * 格式: output_{provider_name}_{request_name}.log
     */
    private static String buildFileName(ProviderEnum provider, RequestType requestType) {
        return String.format("output_%s_%s.log",
                provider.getProviderName(),
                requestType.getRequestName());
    }

    /**
     * 读取流式日志文件并解析为chunk列表
     * (兼容 SSE "data: " 和 Ollama 的纯 JSON 行)
     *
     * @param provider 提供者
     * @param fileName 文件名
     * @return chunk列表
     */
    private static List<String> readStreamingLogFile(ProviderEnum provider, String fileName) {
        List<String> chunks = new ArrayList<>();
        String filePath = RESOURCE_BASE_PATH + provider.getProviderName() + "/" + fileName;

        try (InputStream inputStream = TestDataReader.class.getResourceAsStream(filePath)) {
            Objects.requireNonNull(inputStream, "Test data file not found: " + filePath);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    // 跳过空行
                    if (line.isEmpty()) {
                        continue;
                    }

                    // 1. 处理SSE格式的数据行 (OpenAI, DashScope, Ark)
                    if (line.startsWith("data: ")) {
                        String jsonData = line.substring(6).trim(); // 去除 "data: " 前缀
                        // 确保不是 "data: " 后面跟空
                        if (!jsonData.isEmpty()) {
                            chunks.add(jsonData);
                        }
                    }
                    // 2. 处理 Ollama 格式的流式数据 (每行一个完整 JSON)
                    else if (line.startsWith("{") && line.endsWith("}")) {
                        chunks.add(line);
                    }
                    // 其他行（如 SSE 的注释行 ':' 或 结束标记 [DONE]）将被忽略
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to read streaming log file: " + filePath, e);
        }

        return chunks;
    }

    /**
     * 读取阻塞式日志文件
     *
     * @param provider 提供者
     * @param fileName 文件名
     * @return 完整的 JSON 字符串
     */
    private static String readBlockingLogFile(ProviderEnum provider, String fileName) {
        StringBuilder content = new StringBuilder();
        String filePath = RESOURCE_BASE_PATH + provider.getProviderName() + "/" + fileName;

        try (InputStream inputStream = TestDataReader.class.getResourceAsStream(filePath)) {
            Objects.requireNonNull(inputStream, "Test data file not found: " + filePath);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    // 拼接并保留换行符，以防是多行json（尽管不常见）
                    content.append(line).append(System.lineSeparator());
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to read blocking log file: " + filePath, e);
        }

        return content.toString().trim();
    }
}