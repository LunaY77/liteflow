package com.yomahub.liteflow.test.ai.model.tool.openai;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.MessageType;
import com.yomahub.liteflow.ai.engine.model.chat.message.ToolMessage;
import com.yomahub.liteflow.ai.engine.model.chat.message.UserMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.engine.tool.registry.ScanningToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatModel;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatRequest;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockConfigHolder;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockInteractClient;
import io.reactivex.rxjava3.core.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void testToolCallStreamingWithAutoToolCall() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL,
                TestDataReader.RequestType.STREAMING_TOOL_CALL_2);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("调用工具组装 QQ 和 微信"));

        ToolRegistry assembleTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("assemble_tool")));

        Flowable<ChunkEvent> stream = Flowable.fromPublisher(chatModelWithAutoToolCall.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(assembleTool)
                        .build()
        ));

        ChatResponse response = stream.blockingLast()
                .getFinalResponse();

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
        ToolMessage toolMessage = toolRegistry.executeToolCall(toolCall);


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
    public void testToolCallStreamingWithManualToolCall() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL);

        List<Message> messages = new ArrayList<>();
        messages.add(
                new UserMessage("调用工具组装 QQ 和 微信"));

        ToolRegistry assembleTool = new StaticToolRegistry(
                Collections.singletonList(toolRegistry.getTool("assemble_tool")));

        Flowable<ChunkEvent> stream = Flowable.fromPublisher(chatModelWithManualToolCall.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(assembleTool)
                        .build()
        ));

        ChatResponse response = stream.blockingLast()
                .getFinalResponse();

        Assertions.assertEquals(FinishReason.TOOL_CALL, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
        Assertions.assertTrue(response.hasToolCalls());

        // 获取 ToolCall
        ToolCall toolCall = response.getOutput().getToolCalls().get(0);

        Assertions.assertEquals("assemble_tool", toolCall.getName());

        // 执行 ToolCall
        ToolMessage toolMessage = toolRegistry.executeToolCall(toolCall);

        // 和 AI 消息一起添加回上下文
        messages.add(response.getOutput());
        messages.add(toolMessage);

        // 第二次对话
        MockConfigHolder.clear();
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL_2);

        stream = Flowable.fromPublisher(chatModelWithManualToolCall.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        // 工具调用配置
                        .toolRegistry(assembleTool)
                        .build()
        ));

        response = stream.blockingLast()
                .getFinalResponse();

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

        chatRequestBuilder = OpenAIChatRequest.builder();
    }
}
