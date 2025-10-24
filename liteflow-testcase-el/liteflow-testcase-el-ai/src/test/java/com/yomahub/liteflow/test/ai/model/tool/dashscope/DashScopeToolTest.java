package com.yomahub.liteflow.test.ai.model.tool.dashscope;

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
import com.yomahub.liteflow.ai.model.dashscope.model.chat.DashScopeChatModel;
import com.yomahub.liteflow.ai.model.dashscope.model.chat.DashScopeChatRequest;
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
 * 阿里百炼 工具调用 测试
 * <p>
 * 请注意！！！在测试代码中，构建的request仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故请求中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class DashScopeToolTest extends MockAITest {

    DashScopeChatModel chatModelWithAutoToolCall;
    DashScopeChatModel chatModelWithManualToolCall;
    DashScopeChatRequest.Builder chatRequestBuilder;
    // 工具注册中心，扫描指定包下的工具类
    static final ToolRegistry toolRegistry = new ScanningToolRegistry("com.yomahub.liteflow.test.ai.model.tool.tools");

    @Test
    public void testToolCallWithAutoToolCall() {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_TOOL_CALL, TestDataReader.RequestType.BLOCKING_TOOL_CALL_2);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("北京今天天气怎么样")
        );

        ToolRegistry weatherTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("weather_tool"))
        );

        ChatResponse response = chatModelWithAutoToolCall.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(weatherTool)
                        // 工具调用配置
                        .build()
        );

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertEquals("北京今天天气晴朗，气温为25°C。", response.getOutput().getContent());
    }

    @Test
    public void testToolCallStreamingWithAutoToolCall() throws ExecutionException, InterruptedException {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_TOOL_CALL, TestDataReader.RequestType.STREAMING_TOOL_CALL_2);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("调用工具组装 QQ 和 微信")
        );

        ToolRegistry assembleTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("assemble_tool"))
        );

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
                        .build()
        );

        ChatResponse response = future.get();

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertEquals(
                "<think>\n" +
                        "好的，现在用户让我调用工具组装QQ和微信。首先，我需要确认工具的正确用法。根据提供的工具描述，assemble_tool需要两个字符串参数a和b，将它们组合成答案。\n" +
                        "\n" +
                        "用户给的输入是“QQ”和“微信”，所以应该把a设为\"QQ\"，b设为\"微信\"。调用工具后，返回的结果是\"Assembled result: QQ and 微信\"。不过这里可能有点问题，因为工具描述说是“组装”，可能是指直接拼接，比如QQ微信，但返回的结果里用了“and”连接。不过按照工具返回的字符串，我需要按照实际结果来回应。\n" +
                        "\n" +
                        "用户可能希望得到组合后的结果，比如“QQ微信”，但工具返回的是“QQ and 微信”。不过根据工具的响应，我应该直接使用返回的结果。所以正确的回答应该是“QQ and 微信”。不过可能用户需要的是没有“and”的拼接，但根据工具的输出，可能工具内部处理了，所以应该按工具返回的来。\n" +
                        "\n" +
                        "检查之前的工具调用，参数是否正确。a是QQ，b是微信，所以工具返回的应该是这两个的组合。可能工具内部是把a和b用“and”连接起来，所以结果就是“QQ and 微信”。需要确认用户是否需要这个格式，但根据工具的响应，应该直接使用返回的结果。\n" +
                        "\n" +
                        "所以最终答案应该是“QQ and 微信”</think>\n" +
                        "QQ and 微信",
                response.getOutput().getContent()
        );
    }

    @Test
    public void testToolCallWithManualToolCall() {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_TOOL_CALL);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("北京今天天气怎么样")
        );

        ToolRegistry weatherTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("weather_tool"))
        );

        ChatResponse response = chatModelWithManualToolCall.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(weatherTool)
                        // 工具调用配置
                        .build()
        );

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
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_TOOL_CALL_2);
        response = chatModelWithManualToolCall.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(weatherTool)
                        // 工具调用配置
                        .build()
        );

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertEquals("北京今天天气晴朗，气温为25°C。", response.getOutput().getContent());
    }

    @Test
    public void testToolCallStreamingWithManualToolCall() throws ExecutionException, InterruptedException {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_TOOL_CALL);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("调用工具组装 QQ 和 微信")
        );

        ToolRegistry assembleTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("assemble_tool"))
        );

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
                        .build()
        );

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
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_TOOL_CALL_2);

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
                        .build()
        );

        response = future2.get();

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertEquals(
                "<think>\n" +
                        "好的，现在用户让我调用工具组装QQ和微信。首先，我需要确认工具的正确用法。根据提供的工具描述，assemble_tool需要两个字符串参数a和b，将它们组合成答案。\n" +
                        "\n" +
                        "用户给的输入是“QQ”和“微信”，所以应该把a设为\"QQ\"，b设为\"微信\"。调用工具后，返回的结果是\"Assembled result: QQ and 微信\"。不过这里可能有点问题，因为工具描述说是“组装”，可能是指直接拼接，比如QQ微信，但返回的结果里用了“and”连接。不过按照工具返回的字符串，我需要按照实际结果来回应。\n" +
                        "\n" +
                        "用户可能希望得到组合后的结果，比如“QQ微信”，但工具返回的是“QQ and 微信”。不过根据工具的响应，我应该直接使用返回的结果。所以正确的回答应该是“QQ and 微信”。不过可能用户需要的是没有“and”的拼接，但根据工具的输出，可能工具内部处理了，所以应该按工具返回的来。\n" +
                        "\n" +
                        "检查之前的工具调用，参数是否正确。a是QQ，b是微信，所以工具返回的应该是这两个的组合。可能工具内部是把a和b用“and”连接起来，所以结果就是“QQ and 微信”。需要确认用户是否需要这个格式，但根据工具的响应，应该直接使用返回的结果。\n" +
                        "\n" +
                        "所以最终答案应该是“QQ and 微信”</think>\n" +
                        "QQ and 微信",
                response.getOutput().getContent()
        );
    }


    @BeforeEach
    public void setup() {
        // 构建带有自动工具调用能力的模型实例
        chatModelWithAutoToolCall = DashScopeChatModel.builder()
                // 仅填写必要参数，保证校验通过
                .model("mock-model")
                .apiKey("mock-api-key")
                .apiUrl("mock-api-url")
                .endPoint("mock-end-point")
                // 使用 Mock 交互客户端
                .interactClient(new MockInteractClient())
                .build();

        // 构建带有手动工具调用能力的模型实例
        chatModelWithManualToolCall = DashScopeChatModel.builder()
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

        chatRequestBuilder = DashScopeChatRequest.builder()
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
