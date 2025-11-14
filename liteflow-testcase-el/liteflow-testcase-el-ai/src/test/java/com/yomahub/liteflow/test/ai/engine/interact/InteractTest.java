package com.yomahub.liteflow.test.ai.engine.interact;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.chunk.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.chunk.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.model.dashscope.interact.DashScopeProtocolTransformer;
import com.yomahub.liteflow.ai.model.ollama.interact.OllamaProtocolTransformer;
import com.yomahub.liteflow.ai.model.openai.interact.OpenAIProtocolTransformer;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 交互处理器测试类
 * 测试各种协议转换器和流式处理管道的功能
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class InteractTest {

    private ProtocolTransformer protocolTransformer;
    private InteractContext context;

    /**
     * 创建协议转换器的工厂方法
     *
     * @param provider 提供者枚举
     * @return 对应的协议转换器实例
     */
    private ProtocolTransformer createProtocolTransformer(ProviderEnum provider) {
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

    @Nested
    @DisplayName("OpenAI协议转换器测试")
    class OpenAITests {

        @BeforeEach
        void setUp() {
            context = new InteractContext();
            protocolTransformer = createProtocolTransformer(ProviderEnum.OPENAI);
        }

        @Test
        @DisplayName("测试OpenAI流式文本响应转换")
        void testStreamingTextResponse() {
            testStreamingResponse(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TEXT, false);
        }

        @Test
        @DisplayName("测试OpenAI流式工具调用响应转换")
        void testStreamingToolCallResponse() {
            testStreamingResponse(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL, true);
        }

        @Test
        @DisplayName("测试OpenAI流式结构化响应转换")
        void testStreamingStructuredResponse() {
            testStreamingResponse(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_STRUCTURED, false);
        }

        @Test
        @DisplayName("测试OpenAI阻塞式文本响应转换")
        void testBlockingTextResponse() {
            testBlockingResponse(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_TEXT, false);
        }

        @Test
        @DisplayName("测试OpenAI阻塞式工具调用响应转换")
        void testBlockingToolCallResponse() {
            testBlockingResponse(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_TOOL_CALL, true);
        }

        @Test
        @DisplayName("测试OpenAI阻塞式结构化响应转换")
        void testBlockingStructuredResponse() {
            testBlockingResponse(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_STRUCTURED, false);
        }
    }

    @Nested
    @DisplayName("DashScope协议转换器测试")
    class DashScopeTests {

        @BeforeEach
        void setUp() {
            context = new InteractContext();
            protocolTransformer = createProtocolTransformer(ProviderEnum.DASHSCOPE);
        }

        @Test
        @DisplayName("测试DashScope流式文本响应转换")
        void testStreamingTextResponse() {
            testStreamingResponse(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_TEXT, false);
        }

        @Test
        @DisplayName("测试DashScope流式工具调用响应转换")
        void testStreamingToolCallResponse() {
            testStreamingResponse(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_TOOL_CALL, true);
        }

        @Test
        @DisplayName("测试DashScope流式结构化响应转换")
        void testStreamingStructuredResponse() {
            testStreamingResponse(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_STRUCTURED, false);
        }

        @Test
        @DisplayName("测试DashScope阻塞式文本响应转换")
        void testBlockingTextResponse() {
            testBlockingResponse(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_TEXT, false);
        }

        @Test
        @DisplayName("测试DashScope阻塞式工具调用响应转换")
        void testBlockingToolCallResponse() {
            testBlockingResponse(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_TOOL_CALL, true);
        }

        @Test
        @DisplayName("测试DashScope阻塞式结构化响应转换")
        void testBlockingStructuredResponse() {
            testBlockingResponse(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_STRUCTURED, false);
        }
    }

    @Nested
    @DisplayName("Ollama协议转换器测试")
    class OllamaTests {

        @BeforeEach
        void setUp() {
            context = new InteractContext();
            protocolTransformer = createProtocolTransformer(ProviderEnum.OLLAMA);
        }

        @Test
        @DisplayName("测试Ollama流式文本响应转换")
        void testStreamingTextResponse() {
            testStreamingResponse(ProviderEnum.OLLAMA, TestDataReader.RequestType.STREAMING_TEXT, false);
        }

        @Test
        @DisplayName("测试Ollama流式工具调用响应转换")
        void testStreamingToolCallResponse() {
            testStreamingResponse(ProviderEnum.OLLAMA, TestDataReader.RequestType.STREAMING_TOOL_CALL, true);
        }

        @Test
        @DisplayName("测试Ollama流式结构化响应转换")
        void testStreamingStructuredResponse() {
            testStreamingResponse(ProviderEnum.OLLAMA, TestDataReader.RequestType.STREAMING_STRUCTURED, false);
        }

        @Test
        @DisplayName("测试Ollama阻塞式文本响应转换")
        void testBlockingTextResponse() {
            testBlockingResponse(ProviderEnum.OLLAMA, TestDataReader.RequestType.BLOCKING_TEXT, false);
        }

        @Test
        @DisplayName("测试Ollama阻塞式工具调用响应转换")
        void testBlockingToolCallResponse() {
            testBlockingResponse(ProviderEnum.OLLAMA, TestDataReader.RequestType.BLOCKING_TOOL_CALL, true);
        }

        @Test
        @DisplayName("测试Ollama阻塞式结构化响应转换")
        void testBlockingStructuredResponse() {
            testBlockingResponse(ProviderEnum.OLLAMA, TestDataReader.RequestType.BLOCKING_STRUCTURED, false);
        }
    }

    @Nested
    @DisplayName("参数化测试 - 所有协议转换器")
    class ParameterizedTests {

        @ParameterizedTest
        @EnumSource(value = ProviderEnum.class, names = { "OPENAI", "DASHSCOPE", "OLLAMA" })
        @DisplayName("参数化测试协议转换器提供者名称")
        void testProtocolTransformerProviderEnumName(ProviderEnum provider) {
            ProtocolTransformer transformer = createProtocolTransformer(provider);
            assertEquals(provider.getProviderName(), transformer.getProviderName(),
                    provider.getProviderName() + "协议转换器的提供者名称应为'" + provider.getProviderName() + "'");
        }
    }

    /**
     * 测试流式响应处理的通用方法
     *
     * @param provider     提供者
     * @param requestType 响应类型
     * @param hasToolCalls 是否包含工具调用
     */
    private void testStreamingResponse(ProviderEnum provider, TestDataReader.RequestType requestType, boolean hasToolCalls) {
        // 重新构建pipeline
        context = new InteractContext();
        protocolTransformer = createProtocolTransformer(provider);

        // 读取测试数据
        List<String> streamChunks = TestDataReader.getStreamingChunks(provider, requestType);
        assertFalse(streamChunks.isEmpty(), provider.getProviderName() + "流式" + requestType.getRequestName() + "响应数据不应为空");

        // 处理流式数据
        for (String streamChunk : streamChunks) {
            StreamingProtocolChunk chunk = protocolTransformer.transformStreamingChunk(streamChunk, context);
            updateContextFromChunk(context, chunk);
        }

        // 获取最终响应
        ChatResponse chatResponse = protocolTransformer.transformStreamingResponse(context);

        // 验证响应
        assertNotNull(chatResponse, "聊天响应不应为null");
        assertNotNull(chatResponse.getOutput(), "响应输出不应为null");
        assertNotNull(chatResponse.getOutput(), "输出应为AssistantMessage类型");

        AssistantMessage message = chatResponse.getOutput();

        // 验证工具调用
        if (hasToolCalls) {
            assertTrue(chatResponse.hasToolCalls(), requestType.getRequestName() + "响应应包含工具调用");
            assertNotNull(message.getToolCalls(), "工具调用列表不应为null");
            assertFalse(message.getToolCalls().isEmpty(), "工具调用列表不应为空");

            // 验证工具调用内容
            for (ToolCall toolCall : message.getToolCalls()) {
                if (!provider.equals(ProviderEnum.OLLAMA)) {
                    assertNotNull(toolCall.getId(), "工具调用ID不应为null");
                }
                assertNotNull(toolCall.getName(), "工具调用名称不应为null");
                assertNotNull(toolCall.getType(), "工具调用类型不应为null");
            }
        } else {
            // 对于非工具调用响应，验证内容存在
            assertNotNull(message.getContent(), "消息内容不应为null");
            // 注意：某些情况下内容可能为空字符串，所以这里不强制要求非空
        }

        // 打印测试结果
        System.out.println("=== " + provider.getProviderName() + " 流式" + requestType.getRequestName() + "响应测试结果 ===");
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
    private void testBlockingResponse(ProviderEnum provider, TestDataReader.RequestType responseType, boolean hasToolCalls) {
        // 重新构建pipeline
        context = new InteractContext();
        protocolTransformer = createProtocolTransformer(provider);

        // 读取测试数据
        String blockingResponse = TestDataReader.getBlockingResponse(provider, responseType);
        assertNotNull(blockingResponse, provider.getProviderName() + "阻塞式" + responseType.getRequestName() + "响应数据不应为null");
        assertFalse(blockingResponse.trim().isEmpty(), provider.getProviderName() + "阻塞式" + responseType.getRequestName() + "响应数据不应为空");

        // 处理阻塞式响应
        ChatResponse chatResponse = protocolTransformer.transformBlockingResponse(blockingResponse, context);

        // 验证响应
        assertNotNull(chatResponse, "聊天响应不应为null");
        assertNotNull(chatResponse.getOutput(), "响应输出不应为null");
        assertNotNull(chatResponse.getOutput(), "输出应为AssistantMessage类型");

        AssistantMessage message = chatResponse.getOutput();

        // 验证工具调用
        if (hasToolCalls) {
            assertTrue(chatResponse.hasToolCalls(), responseType.getRequestName() + "响应应包含工具调用");
            assertNotNull(message.getToolCalls(), "工具调用列表不应为null");
            assertFalse(message.getToolCalls().isEmpty(), "工具调用列表不应为空");

            // 验证每个工具调用
            for (ToolCall toolCall : message.getToolCalls()) {
                if (!provider.equals(ProviderEnum.OLLAMA)) {
                    assertNotNull(toolCall.getId(), "工具调用ID不应为null");
                }
                assertNotNull(toolCall.getName(), "工具调用名称不应为null");
                assertNotNull(toolCall.getType(), "工具调用类型不应为null");
                assertEquals("function", toolCall.getType(), "工具调用类型应为function");
            }
        } else {
            // 对于非工具调用响应，验证内容存在
            assertNotNull(message.getContent(), "消息内容不应为null");
            // 注意：某些情况下内容可能为空字符串，所以这里不强制要求非空
        }

        // 验证token使用情况（如果存在）
        if (chatResponse.getTokenUsage() != null) {
            assertTrue(chatResponse.getTokenUsage().getTotalTokenCount() > 0, "总token数应大于0");
        }

        // 打印测试结果
        System.out.println("=== " + provider.getProviderName() + " 阻塞式" + responseType.getRequestName() + "响应测试结果 ===");
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

    /**
     * 根据分块信息更新交互上下文
     *
     * @param context 交互上下文
     * @param chunk   协议分块
     */
    private void updateContextFromChunk(InteractContext context, StreamingProtocolChunk chunk) {
        if (Objects.isNull(chunk)) {
            return;
        }

        switch (chunk.getType()) {
            case TEXT:
                context.addText((String) chunk.getData());
                break;
            case THINKING:
                context.addThinking((String) chunk.getData());
                break;
            default:
                // 其他类型暂不处理
                break;
        }
    }
}
