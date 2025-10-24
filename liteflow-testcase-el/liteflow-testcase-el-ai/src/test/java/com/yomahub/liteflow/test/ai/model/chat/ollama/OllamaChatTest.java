package com.yomahub.liteflow.test.ai.model.chat.ollama;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.*;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatRequest;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockInteractClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Ollama chat 测试
 * <p>
 * 请注意！！！在测试代码中，构建的request仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故请求中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class OllamaChatTest extends MockAITest {

    OllamaChatModel chatModel;
    OllamaChatRequest.Builder chatRequestBuilder;

    @Test
    public void testBlocking() {
        setupChatMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.BLOCKING_TEXT);

        List<Message> messages = Arrays.asList(
                new SystemMessage("You are a helpful assistant."),
                new UserMessage("why is the sky blue?")
        );

        ChatResponse response = chatModel.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        .build()
        );

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
    }

    @Test
    public void testStreaming() throws ExecutionException, InterruptedException {
        setupChatMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.STREAMING_TEXT);

        List<Message> messages = Arrays.asList(
                new SystemMessage("You are a helpful assistant."),
                new UserMessage("why is the sky blue?")
        );

        final CompletableFuture<ChatResponse> future = new CompletableFuture<>();

        chatModel.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.DN_JSON)
                        .messages(messages)
                        .onFinal((chatResponse, context) -> {
                            future.complete(chatResponse);
                            return chatResponse;
                        })
                        .build()
        );

        ChatResponse response = future.get();

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
    }

    @BeforeEach
    public void setup() {
        chatModel = OllamaChatModel.builder()
                // 仅填写必要参数，保证校验通过
                .model("mock-model")
                .apiUrl("mock-api-url")
                .endPoint("mock-end-point")
                // 使用 Mock 交互客户端
                .interactClient(new MockInteractClient())
                .build();

        chatRequestBuilder = OllamaChatRequest.builder()
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
