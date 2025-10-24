package com.yomahub.liteflow.test.ai.model.tool.openai;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.*;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.registry.ScanningToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatModel;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatRequest;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockConfigHolder;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockInteractClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * OpenAI 工具调用 测试
 * <p>
 * 请注意！！！在测试代码中，构建的request仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故请求中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class OpenAIToolTest extends MockAITest {

    OpenAIChatModel chatModelWithAutoToolCall;
    OpenAIChatModel chatModelWithManualToolCall;
    OpenAIChatRequest.Builder chatRequestBuilder;
    // 工具注册中心，扫描指定包下的工具类
    static final ToolRegistry toolRegistry = new ScanningToolRegistry("com.yomahub.liteflow.test.ai.model.tool.tools");

    @Test
    public void testToolCallWithAutoToolCall() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_TOOL_CALL,
                TestDataReader.RequestType.BLOCKING_TOOL_CALL_2);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("北京今天天气怎么样"));

        ToolRegistry weatherTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("weather_tool")));

        ChatResponse response = chatModelWithAutoToolCall.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(weatherTool)
                        // 工具调用配置
                        .build());

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertEquals("北京今天天气晴朗，气温25°C。", response.getOutput().getContent());
    }

    @Test
    public void testToolCallStreamingWithAutoToolCall() throws ExecutionException, InterruptedException {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL,
                TestDataReader.RequestType.STREAMING_TOOL_CALL_2);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("调用工具组装 QQ 和 微信"));

        ToolRegistry assembleTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("assemble_tool")));

        final CompletableFuture<ChatResponse> future = new CompletableFuture<>();

        chatModelWithAutoToolCall.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(assembleTool)
                        // 工具调用配置
                        .onFinal((chatResponse, context) -> {
                            future.complete(chatResponse);
                            return chatResponse;
                        })
                        .build());

        ChatResponse response = future.get();

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertTrue(response.getOutput().getContent().contains("QQ and微信"));
    }

    @Test
    public void testToolCallWithManualToolCall() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_TOOL_CALL);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("北京今天天气怎么样"));

        ToolRegistry weatherTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("weather_tool")));

        ChatResponse response = chatModelWithManualToolCall.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(weatherTool)
                        // 工具调用配置
                        .build());

        Assertions.assertEquals(FinishReason.TOOL_CALL, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertTrue(response.hasToolCalls());

        // 获取 ToolCall
        ToolCall toolCall = response.getOutput().getToolCalls().get(0);

        Assertions.assertEquals("weather_tool", toolCall.getName());

        // 执行 ToolCall
        ToolCallBack toolCallBack = weatherTool.getAllTools()
                .stream()
                .filter(tool -> Objects.equals(tool.getName(), toolCall.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("工具未注册: " + toolCall.getName()));
        String toolResult = toolCallBack.call(toolCall.getArguments().toString());
        ToolMessage toolMessage = new ToolMessage(toolResult, toolCall.getId(), toolCall.getName());

        // 和 AI 消息一起添加回上下文
        messages.add(response.getOutput());
        messages.add(toolMessage);

        // 第二次对话
        MockConfigHolder.clear();
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_TOOL_CALL_2);
        response = chatModelWithManualToolCall.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(weatherTool)
                        // 工具调用配置
                        .build());

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertEquals("北京今天天气晴朗，气温25°C。", response.getOutput().getContent());
    }

    @Test
    public void testToolCallStreamingWithManualToolCall() throws ExecutionException, InterruptedException {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("调用工具组装 QQ 和 微信"));

        ToolRegistry assembleTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("assemble_tool")));

        final CompletableFuture<ChatResponse> future = new CompletableFuture<>();

        chatModelWithManualToolCall.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(assembleTool)
                        // 工具调用配置
                        .onFinal((chatResponse, context) -> {
                            future.complete(chatResponse);
                            return chatResponse;
                        })
                        .build());

        ChatResponse response = future.get();

        Assertions.assertEquals(FinishReason.TOOL_CALL, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertTrue(response.hasToolCalls());

        // 获取 ToolCall
        ToolCall toolCall = response.getOutput().getToolCalls().get(0);

        Assertions.assertEquals("assemble_tool", toolCall.getName());

        // 执行 ToolCall
        ToolCallBack toolCallBack = assembleTool.getAllTools()
                .stream()
                .filter(tool -> Objects.equals(tool.getName(), toolCall.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("工具未注册: " + toolCall.getName()));
        String toolResult = toolCallBack.call(toolCall.getArguments().toString());
        ToolMessage toolMessage = new ToolMessage(toolResult, toolCall.getId(), toolCall.getName());

        // 和 AI 消息一起添加回上下文
        messages.add(response.getOutput());
        messages.add(toolMessage);

        // 第二次对话
        MockConfigHolder.clear();
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL_2);

        final CompletableFuture<ChatResponse> future2 = new CompletableFuture<>();

        chatModelWithManualToolCall.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(assembleTool)
                        // 工具调用配置
                        .onFinal((chatResponse, context) -> {
                            future2.complete(chatResponse);
                            return chatResponse;
                        })
                        .build());

        response = future2.get();

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertTrue(response.getOutput().getContent().contains("QQ and微信"));
    }

    @BeforeEach
    public void setup() {
        // 构建带有自动工具调用能力的模型实例
        chatModelWithAutoToolCall = OpenAIChatModel.builder()
                // 仅填写必要参数，保证校验通过
                .model("mock-model")
                .apiKey("mock-api-key")
                .apiUrl("mock-api-url")
                .endPoint("mock-end-point")
                // 使用 Mock 交互客户端
                .interactClient(new MockInteractClient())
                .build();

        // 构建带有手动工具调用能力的模型实例
        chatModelWithManualToolCall = OpenAIChatModel.builder()
                // 默认为 true，框架会自动调用工具，这里设置为 false，表示需要用户手动处理工具调用
                .autoToolCallEnabled(false)
                // 仅填写必要参数，保证校验通过
                .model("mock-model")
                .apiKey("mock-api-key")
                .apiUrl("mock-api-url")
                .endPoint("mock-end-point")
                // 使用 Mock 交互客户端
                .interactClient(new MockInteractClient())
                .build();

        chatRequestBuilder = OpenAIChatRequest.builder()
                .onStart(context -> System.out.println("chat start"))
                .onClose(context -> System.out.println("chat close"))
                .onError((context, t) -> {
                    throw new RuntimeException(t);
                })
                .onText((content, context) -> {
                    System.out.println("Received text: " + content);
                    return content;
                })
                .onThinking((content, context) -> {
                    System.out.println("Received thinking: " + content);
                    return content;
                })
                .onCompletion((response, context) -> {
                    AssistantMessage message = response.getOutput();
                    if (message.getContent() != null && !message.getContent().trim().isEmpty()) {
                        System.out.println("内容长度: " + message.getContent().length());
                        if (message.getContent().length() > 200) {
                            System.out.println("内容预览: " + message.getContent().substring(0, 200) + "...");
                        } else {
                            System.out.println("内容: " + message.getContent());
                        }
                    }
                    if (response.hasToolCalls()) {
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
                    System.out.println("Token使用情况: " + response.getTokenUsage());
                    System.out.println("完成原因: " + response.getFinishReason());
                    return response;
                });
    }
}
