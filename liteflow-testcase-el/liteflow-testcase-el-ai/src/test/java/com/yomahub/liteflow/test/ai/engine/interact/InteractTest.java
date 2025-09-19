package com.yomahub.liteflow.test.ai.engine.interact;

import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.model.dashscope.interact.DashScopeProtocolTransformer;
import com.yomahub.liteflow.ai.model.ollama.interact.OllamaProtocolTransformer;
import com.yomahub.liteflow.ai.model.openai.interact.OpenAIProtocolTransformer;
import com.yomahub.liteflow.test.ai.engine.interact.util.TestDataReader;
import com.yomahub.liteflow.test.ai.engine.interact.util.TestDataReader.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 交互处理器测试类
 * 测试各种协议转换器和流式处理管道的功能
 *
 * @author 苍镜月
 * @since 2.5.0
 */
public class InteractTest {

    private ChunkProcessPipeline pipeline;
    private ProtocolTransformer protocolTransformer;
    private ChunkCallbackTransformer chunkCallbackTransformer;
    private InteractContext context;

    /**
     * 创建协议转换器的工厂方法
     *
     * @param provider 提供者枚举
     * @return 对应的协议转换器实例
     */
    private ProtocolTransformer createProtocolTransformer(Provider provider) {
        switch (provider) {
            case OPENAI:
                return new OpenAIProtocolTransformer();
            case DASHSCOPE:
                return new DashScopeProtocolTransformer();
            case OLLAMA:
                return new OllamaProtocolTransformer();
            default:
                throw new IllegalArgumentException("不支持的提供者: " + provider);
        }
    }

    /**
     * 简单的回调处理器，用于测试
     */
    private static class TestChunkCallbackTransformer implements ChunkCallbackTransformer {
        @Override
        public String onText(String content, InteractContext context) {
            System.out.println("onText: " + content);
            return content;
        }

        @Override
        public String onThinking(String content, InteractContext context) {
            System.out.println("onThinking: " + content);
            return content;
        }

        @Override
        public List<ToolCall> onToolsCalling(List<ToolCall> toolCalls, InteractContext context) {
            // 测试用的简单实现，直接返回原工具调用
            return toolCalls;
        }

        @Override
        public Object onUsage(Object content, InteractContext context) {
            // 测试用的简单实现，直接返回原使用统计
            return content;
        }

        @Override
        public Object onGrounding(Object content, InteractContext context) {
            // 测试用的简单实现，直接返回原基础信息
            return content;
        }
    }

    @Nested
    @DisplayName("OpenAI协议转换器测试")
    class OpenAITests {

        @BeforeEach
        void setUp() {
            context = new InteractContext();
            protocolTransformer = createProtocolTransformer(Provider.OPENAI);
            chunkCallbackTransformer = new TestChunkCallbackTransformer();
        }

        @Test
        @DisplayName("测试OpenAI流式文本响应转换")
        void testStreamingTextResponse() {
            testStreamingResponse(Provider.OPENAI, "text", false);
        }

        @Test
        @DisplayName("测试OpenAI流式工具调用响应转换")
        void testStreamingToolCallResponse() {
            testStreamingResponse(Provider.OPENAI, "tool_call", true);
        }

        @Test
        @DisplayName("测试OpenAI流式结构化响应转换")
        void testStreamingStructuredResponse() {
            testStreamingResponse(Provider.OPENAI, "structured", false);
        }

        @Test
        @DisplayName("测试OpenAI阻塞式文本响应转换")
        void testBlockingTextResponse() {
            testBlockingResponse(Provider.OPENAI, "text", false);
        }

        @Test
        @DisplayName("测试OpenAI阻塞式工具调用响应转换")
        void testBlockingToolCallResponse() {
            testBlockingResponse(Provider.OPENAI, "tool_call", true);
        }

        @Test
        @DisplayName("测试OpenAI阻塞式结构化响应转换")
        void testBlockingStructuredResponse() {
            testBlockingResponse(Provider.OPENAI, "structured", false);
        }
    }

    @Nested
    @DisplayName("DashScope协议转换器测试")
    class DashScopeTests {

        @BeforeEach
        void setUp() {
            context = new InteractContext();
            protocolTransformer = createProtocolTransformer(Provider.DASHSCOPE);
            chunkCallbackTransformer = new TestChunkCallbackTransformer();
        }

        @Test
        @DisplayName("测试DashScope流式文本响应转换")
        void testStreamingTextResponse() {
            testStreamingResponse(Provider.DASHSCOPE, "text", false);
        }

        @Test
        @DisplayName("测试DashScope流式工具调用响应转换")
        void testStreamingToolCallResponse() {
            testStreamingResponse(Provider.DASHSCOPE, "tool_call", true);
        }

        @Test
        @DisplayName("测试DashScope流式结构化响应转换")
        void testStreamingStructuredResponse() {
            testStreamingResponse(Provider.DASHSCOPE, "structured", false);
        }

        @Test
        @DisplayName("测试DashScope阻塞式文本响应转换")
        void testBlockingTextResponse() {
            testBlockingResponse(Provider.DASHSCOPE, "text", false);
        }

        @Test
        @DisplayName("测试DashScope阻塞式工具调用响应转换")
        void testBlockingToolCallResponse() {
            testBlockingResponse(Provider.DASHSCOPE, "tool_call", true);
        }

        @Test
        @DisplayName("测试DashScope阻塞式结构化响应转换")
        void testBlockingStructuredResponse() {
            testBlockingResponse(Provider.DASHSCOPE, "structured", false);
        }
    }

    @Nested
    @DisplayName("Ollama协议转换器测试")
    class OllamaTests {

        @BeforeEach
        void setUp() {
            context = new InteractContext();
            protocolTransformer = createProtocolTransformer(Provider.OLLAMA);
            chunkCallbackTransformer = new TestChunkCallbackTransformer();
        }

        @Test
        @DisplayName("测试Ollama流式文本响应转换")
        void testStreamingTextResponse() {
            testStreamingResponse(Provider.OLLAMA, "text", false);
        }

        @Test
        @DisplayName("测试Ollama流式工具调用响应转换")
        void testStreamingToolCallResponse() {
            testStreamingResponse(Provider.OLLAMA, "tool_call", true);
        }

        @Test
        @DisplayName("测试Ollama流式结构化响应转换")
        void testStreamingStructuredResponse() {
            testStreamingResponse(Provider.OLLAMA, "structured", false);
        }

        @Test
        @DisplayName("测试Ollama阻塞式文本响应转换")
        void testBlockingTextResponse() {
            testBlockingResponse(Provider.OLLAMA, "text", false);
        }

        @Test
        @DisplayName("测试Ollama阻塞式工具调用响应转换")
        void testBlockingToolCallResponse() {
            testBlockingResponse(Provider.OLLAMA, "tool_call", true);
        }

        @Test
        @DisplayName("测试Ollama阻塞式结构化响应转换")
        void testBlockingStructuredResponse() {
            testBlockingResponse(Provider.OLLAMA, "structured", false);
        }
    }

    @Nested
    @DisplayName("参数化测试 - 所有协议转换器")
    class ParameterizedTests {

        @ParameterizedTest
        @EnumSource(value = Provider.class, names = { "OPENAI", "DASHSCOPE", "OLLAMA" })
        @DisplayName("参数化测试协议转换器提供者名称")
        void testProtocolTransformerProviderName(Provider provider) {
            ProtocolTransformer transformer = createProtocolTransformer(provider);
            assertEquals(provider.getName(), transformer.getProviderName(),
                    provider.getName() + "协议转换器的提供者名称应为'" + provider.getName() + "'");
        }
    }

    /**
     * 测试流式响应处理的通用方法
     *
     * @param provider     提供者
     * @param responseType 响应类型
     * @param hasToolCalls 是否包含工具调用
     */
    private void testStreamingResponse(Provider provider, String responseType, boolean hasToolCalls) {
        // 重新构建pipeline
        context = new InteractContext();
        protocolTransformer = createProtocolTransformer(provider);
        chunkCallbackTransformer = new TestChunkCallbackTransformer();
        pipeline = ChunkProcessPipeline.createStreamingPipeline(context, protocolTransformer, chunkCallbackTransformer);

        // 读取测试数据
        List<String> streamChunks = TestDataReader.getStreamingChunks(provider, responseType);
        assertFalse(streamChunks.isEmpty(), provider.getName() + "流式" + responseType + "响应数据不应为空");

        // 处理流式数据
        for (String streamChunk : streamChunks) {
            pipeline.processStreaming(streamChunk);
            // 注意：这里不强制要求最后一次返回true，因为不同模型的结束标志可能不同
        }

        // 获取最终响应
        ChatResponse chatResponse = pipeline.buildFinalStreamingResponse();

        // 验证响应
        assertNotNull(chatResponse, "聊天响应不应为null");
        assertNotNull(chatResponse.getOutput(), "响应输出不应为null");
        assertTrue(chatResponse.getOutput() instanceof AssistantMessage, "输出应为AssistantMessage类型");

        AssistantMessage message = (AssistantMessage) chatResponse.getOutput();

        // 验证工具调用
        if (hasToolCalls) {
            assertTrue(chatResponse.hasToolCalls(), responseType + "响应应包含工具调用");
            assertNotNull(message.getToolCalls(), "工具调用列表不应为null");
            assertFalse(message.getToolCalls().isEmpty(), "工具调用列表不应为空");

            // 验证工具调用内容
            for (ToolCall toolCall : message.getToolCalls()) {
                if (!provider.equals(Provider.OLLAMA)) {
                    assertNotNull(toolCall.getId(), "工具调用ID不应为null");
                }
                assertNotNull(toolCall.getName(), "工具调用名称不应为null");
                assertNotNull(toolCall.getType(), "工具调用类型不应为null");
            }
        } else {
            // 对于非工具调用响应，验证内容存在
            if (!hasToolCalls && !"tool_call".equals(responseType)) {
                assertNotNull(message.getContent(), "消息内容不应为null");
                // 注意：某些情况下内容可能为空字符串，所以这里不强制要求非空
            }
        }

        // 打印测试结果
        System.out.println("=== " + provider.getName() + " 流式" + responseType + "响应测试结果 ===");
        if (message.getContent() != null && !message.getContent().trim().isEmpty()) {
            System.out.println(
                    "内容: " + (message.getContent().length() > 100 ? message.getContent().substring(0, 100) + "..."
                            : message.getContent()));
        }
        if (hasToolCalls && message.getToolCalls() != null) {
            System.out.println("工具调用数量: " + message.getToolCalls().size());
        }
        System.out.println("Token使用情况: " + chatResponse.getTokenUsage());
        System.out.println("完成原因: " + chatResponse.getFinishReason());
    }

    /**
     * 测试阻塞式响应处理的通用方法
     *
     * @param provider     提供者
     * @param responseType 响应类型
     * @param hasToolCalls 是否包含工具调用
     */
    private void testBlockingResponse(Provider provider, String responseType, boolean hasToolCalls) {
        // 重新构建pipeline
        context = new InteractContext();
        protocolTransformer = createProtocolTransformer(provider);
        pipeline = ChunkProcessPipeline.createBlockingPipeline(context, protocolTransformer);

        // 读取测试数据
        String blockingResponse = TestDataReader.getBlockingResponse(provider, responseType);
        assertNotNull(blockingResponse, provider.getName() + "阻塞式" + responseType + "响应数据不应为null");
        assertFalse(blockingResponse.trim().isEmpty(), provider.getName() + "阻塞式" + responseType + "响应数据不应为空");

        // 处理阻塞式响应
        ChatResponse chatResponse = pipeline.processBlocking(blockingResponse);

        // 验证响应
        assertNotNull(chatResponse, "聊天响应不应为null");
        assertNotNull(chatResponse.getOutput(), "响应输出不应为null");
        assertTrue(chatResponse.getOutput() instanceof AssistantMessage, "输出应为AssistantMessage类型");

        AssistantMessage message = (AssistantMessage) chatResponse.getOutput();

        // 验证工具调用
        if (hasToolCalls) {
            assertTrue(chatResponse.hasToolCalls(), responseType + "响应应包含工具调用");
            assertNotNull(message.getToolCalls(), "工具调用列表不应为null");
            assertFalse(message.getToolCalls().isEmpty(), "工具调用列表不应为空");

            // 验证每个工具调用
            for (ToolCall toolCall : message.getToolCalls()) {
                if (!provider.equals(Provider.OLLAMA)) {
                    assertNotNull(toolCall.getId(), "工具调用ID不应为null");
                }
                assertNotNull(toolCall.getName(), "工具调用名称不应为null");
                assertNotNull(toolCall.getType(), "工具调用类型不应为null");
                assertEquals("function", toolCall.getType(), "工具调用类型应为function");
            }
        } else {
            // 对于非工具调用响应，验证内容存在
            if (!hasToolCalls && !"tool_call".equals(responseType)) {
                assertNotNull(message.getContent(), "消息内容不应为null");
                // 注意：某些情况下内容可能为空字符串，所以这里不强制要求非空
            }
        }

        // 验证token使用情况（如果存在）
        if (chatResponse.getTokenUsage() != null) {
            assertTrue(chatResponse.getTokenUsage().getTotalTokenCount() > 0, "总token数应大于0");
        }

        // 打印测试结果
        System.out.println("=== " + provider.getName() + " 阻塞式" + responseType + "响应测试结果 ===");
        if (message.getContent() != null && !message.getContent().trim().isEmpty()) {
            System.out.println("内容长度: " + message.getContent().length());
            if (message.getContent().length() > 200) {
                System.out.println("内容预览: " + message.getContent().substring(0, 200) + "...");
            } else {
                System.out.println("内容: " + message.getContent());
            }
        }
        if (hasToolCalls && message.getToolCalls() != null) {
            System.out.println("工具调用数量: " + message.getToolCalls().size());
            for (int i = 0; i < message.getToolCalls().size(); i++) {
                ToolCall toolCall = message.getToolCalls().get(i);
                System.out.println("工具调用 " + (i + 1) + ":");
                System.out.println("  ID: " + toolCall.getId());
                System.out.println("  名称: " + toolCall.getName());
                System.out.println("  类型: " + toolCall.getType());
                System.out.println("  参数: " + toolCall.getArguments());
            }
        }
        System.out.println("Token使用情况: " + chatResponse.getTokenUsage());
        System.out.println("完成原因: " + chatResponse.getFinishReason());
    }
}
