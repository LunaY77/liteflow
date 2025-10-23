package com.yomahub.liteflow.test.ai.mock;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;

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
 * @since 2.16.0
 */
public class TestDataReader {

    private static final String RESOURCE_BASE_PATH = "/mock/data/";

    public enum ResponseType {
        TEXT("text"),
        TOOL_CALL("tool_call"),
        STRUCTURED("structured");

        private final String type;

        ResponseType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    // ============= 通用方法 =============

    /**
     * 通用方法：获取流式响应数据
     *
     * @param provider 提供者
     * @param type     响应类型
     * @return 流式响应chunk列表
     */
    public static List<String> getStreamingChunks(ProviderEnum provider, ResponseType type) {
        String fileName = String.format("output_%s_streaming_%s.log", provider.getProviderName(), type.getType());
        return readStreamingLogFile(provider, fileName);
    }

    /**
     * 通用方法：获取阻塞式响应数据
     *
     * @param provider 提供者
     * @param type     响应类型
     * @return 阻塞式响应JSON字符串
     */
    public static String getBlockingResponse(ProviderEnum provider, ResponseType type) {
        String fileName = String.format("output_%s_blocking_%s.log", provider.getProviderName(), type.getType());
        return readBlockingLogFile(provider, fileName);
    }

    /**
     * 读取流式日志文件并解析为chunk列表
     *
     * @param provider 提供者
     * @param fileName 文件名
     * @return chunk列表
     */
    private static List<String> readStreamingLogFile(ProviderEnum provider, String fileName) {
        List<String> chunks = new ArrayList<>();
        String filePath = RESOURCE_BASE_PATH + provider.getProviderName() + "/" + fileName;

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
    private static String readBlockingLogFile(ProviderEnum provider, String fileName) {
        StringBuilder content = new StringBuilder();
        String filePath = RESOURCE_BASE_PATH + provider.getProviderName() + "/" + fileName;

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
