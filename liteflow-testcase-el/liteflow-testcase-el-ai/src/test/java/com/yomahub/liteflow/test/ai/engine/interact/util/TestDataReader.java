package com.yomahub.liteflow.test.ai.engine.interact.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试数据读取工具类
 * 用于读取测试资源文件中的模型响应数据
 *
 * @author 苍镜月
 * @since 2.5.0
 */
public class TestDataReader {

    private static final String RESOURCE_BASE_PATH = "/engine/protocol/";

    public enum Provider {
        OPENAI("openai"),
        DASHSCOPE("dashscope"),
        OLLAMA("ollama");

        private final String name;

        Provider(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // ============= OpenAI 相关方法 =============

    /**
     * 读取OpenAI流式文本响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getOpenAIStreamingTextChunks() {
        return getStreamingChunks(Provider.OPENAI, "text");
    }

    /**
     * 读取OpenAI流式工具调用响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getOpenAIStreamingToolCallChunks() {
        return getStreamingChunks(Provider.OPENAI, "tool_call");
    }

    /**
     * 读取OpenAI流式结构化响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getOpenAIStreamingStructuredChunks() {
        return getStreamingChunks(Provider.OPENAI, "structured");
    }

    /**
     * 读取OpenAI阻塞式文本响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getOpenAIBlockingTextResponse() {
        return getBlockingResponse(Provider.OPENAI, "text");
    }

    /**
     * 读取OpenAI阻塞式工具调用响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getOpenAIBlockingToolCallResponse() {
        return getBlockingResponse(Provider.OPENAI, "tool_call");
    }

    /**
     * 读取OpenAI阻塞式结构化响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getOpenAIBlockingStructuredResponse() {
        return getBlockingResponse(Provider.OPENAI, "structured");
    }

    // ============= DashScope 相关方法 =============

    /**
     * 读取DashScope流式文本响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getDashScopeStreamingTextChunks() {
        return getStreamingChunks(Provider.DASHSCOPE, "text");
    }

    /**
     * 读取DashScope流式工具调用响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getDashScopeStreamingToolCallChunks() {
        return getStreamingChunks(Provider.DASHSCOPE, "tool_call");
    }

    /**
     * 读取DashScope流式结构化响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getDashScopeStreamingStructuredChunks() {
        return getStreamingChunks(Provider.DASHSCOPE, "structured");
    }

    /**
     * 读取DashScope阻塞式文本响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getDashScopeBlockingTextResponse() {
        return getBlockingResponse(Provider.DASHSCOPE, "text");
    }

    /**
     * 读取DashScope阻塞式工具调用响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getDashScopeBlockingToolCallResponse() {
        return getBlockingResponse(Provider.DASHSCOPE, "tool_call");
    }

    /**
     * 读取DashScope阻塞式结构化响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getDashScopeBlockingStructuredResponse() {
        return getBlockingResponse(Provider.DASHSCOPE, "structured");
    }

    // ============= Ollama 相关方法 =============

    /**
     * 读取Ollama流式文本响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getOllamaStreamingTextChunks() {
        return getStreamingChunks(Provider.OLLAMA, "text");
    }

    /**
     * 读取Ollama流式工具调用响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getOllamaStreamingToolCallChunks() {
        return getStreamingChunks(Provider.OLLAMA, "tool_call");
    }

    /**
     * 读取Ollama流式结构化响应数据
     *
     * @return 流式响应chunk列表
     */
    public static List<String> getOllamaStreamingStructuredChunks() {
        return getStreamingChunks(Provider.OLLAMA, "structured");
    }

    /**
     * 读取Ollama阻塞式文本响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getOllamaBlockingTextResponse() {
        return getBlockingResponse(Provider.OLLAMA, "text");
    }

    /**
     * 读取Ollama阻塞式工具调用响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getOllamaBlockingToolCallResponse() {
        return getBlockingResponse(Provider.OLLAMA, "tool_call");
    }

    /**
     * 读取Ollama阻塞式结构化响应数据
     *
     * @return 阻塞式响应JSON字符串
     */
    public static String getOllamaBlockingStructuredResponse() {
        return getBlockingResponse(Provider.OLLAMA, "structured");
    }

    // ============= 通用方法 =============

    /**
     * 通用方法：获取流式响应数据
     *
     * @param provider 提供者
     * @param type     响应类型
     * @return 流式响应chunk列表
     */
    public static List<String> getStreamingChunks(Provider provider, String type) {
        String fileName = String.format("output_%s_streaming_%s.log", provider.getName(), type);
        return readStreamingLogFile(provider, fileName);
    }

    /**
     * 通用方法：获取阻塞式响应数据
     *
     * @param provider 提供者
     * @param type     响应类型
     * @return 阻塞式响应JSON字符串
     */
    public static String getBlockingResponse(Provider provider, String type) {
        String fileName = String.format("output_%s_blocking_%s.log", provider.getName(), type);
        return readBlockingLogFile(provider, fileName);
    }

    /**
     * 读取流式日志文件并解析为chunk列表
     *
     * @param provider 提供者
     * @param fileName 文件名
     * @return chunk列表
     */
    private static List<String> readStreamingLogFile(Provider provider, String fileName) {
        List<String> chunks = new ArrayList<>();
        String filePath = RESOURCE_BASE_PATH + provider.getName() + "/" + fileName;

        try (InputStream inputStream = TestDataReader.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Test data file not found: " + filePath);
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    // 跳过空行
                    if (line.isEmpty()) {
                        continue;
                    }
                    // 处理SSE格式的数据行，去除 "data: " 前缀
                    if (line.startsWith("data: ")) {
                        String jsonData = line.substring(6); // 去除 "data: " 前缀
                        chunks.add(jsonData);
                    } else {
                        // 对于非SSE格式的数据（如Ollama），直接添加
                        chunks.add(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read streaming log file: " + filePath, e);
        }

        return chunks;
    }

    /**
     * 读取阻塞式日志文件
     *
     * @param provider 提供者
     * @param fileName 文件名
     * @return JSON字符串
     */
    private static String readBlockingLogFile(Provider provider, String fileName) {
        StringBuilder content = new StringBuilder();
        String filePath = RESOURCE_BASE_PATH + provider.getName() + "/" + fileName;

        try (InputStream inputStream = TestDataReader.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Test data file not found: " + filePath);
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read blocking log file: " + filePath, e);
        }

        return content.toString().trim();
    }
}
