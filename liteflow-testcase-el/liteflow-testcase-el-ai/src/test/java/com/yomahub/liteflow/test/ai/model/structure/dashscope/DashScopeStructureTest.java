package com.yomahub.liteflow.test.ai.model.structure.dashscope;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.*;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.model.dashscope.model.chat.DashScopeChatModel;
import com.yomahub.liteflow.ai.model.dashscope.model.chat.DashScopeChatRequest;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockInteractClient;
import com.yomahub.liteflow.test.ai.model.structure.output.MathReasoning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 阿里百炼 结构化输出 测试
 * <p>
 * 请注意！！！在测试代码中，构建的request仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故请求中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class DashScopeStructureTest extends MockAITest {

    DashScopeChatModel chatModel;
    DashScopeChatRequest.Builder chatRequestBuilder;

    @Test
    public void testStructure() {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_STRUCTURED);

        List<Message> messages = Arrays.asList(
                new SystemMessage("你是一位数学辅导老师"),
                new UserMessage("使用中文解题: 8x + 9 = 32 and x + y = 1")
        );

        ChatResponse response = chatModel.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 结构化输出相关配置
                        .responseType(ResponseType.JSON)
                        .targetType(MathReasoning.class)
                        // 结构化输出相关配置
                        .build()
        );

        // 将响应转换为结构化结果对象
        MathReasoning result = response.as(MathReasoning.class);

        Assertions.assertNotNull(result);

        System.out.println(result);

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
    }

    @Test
    public void testStructureStreaming() throws ExecutionException, InterruptedException {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_STRUCTURED);

        List<Message> messages = Arrays.asList(
                new SystemMessage("你是一位数学辅导老师"),
                new UserMessage("使用中文解题: 8x + 9 = 32 and x + y = 1")
        );

        final CompletableFuture<ChatResponse> future = new CompletableFuture<>();

        chatModel.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        // 结构化输出相关配置
                        .responseType(ResponseType.JSON)
                        .targetType(MathReasoning.class)
                        // 结构化输出相关配置
                        .onFinal((chatResponse, context) -> {
                            future.complete(chatResponse);
                            return chatResponse;
                        })
                        .build()
        );

        ChatResponse response = future.get();

        // 将响应转换为结构化结果对象
        MathReasoning result = response.as(MathReasoning.class);

        Assertions.assertNotNull(result);

        System.out.println(result);

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
    }

    @BeforeEach
    public void setup() {
        chatModel = DashScopeChatModel.builder()
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
