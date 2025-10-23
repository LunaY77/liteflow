package com.yomahub.liteflow.test.ai.core.chat;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

@TestPropertySource(properties = {"spring.config.location=classpath:core/chat/application.yaml"})
@SpringBootTest(classes = {ChatTest.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.core.chat.cmp"})
public class ChatTest extends MockAITest {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testDashScopeChat() {
        setupMock(ProviderEnum.DASHSCOPE, TestDataReader.ResponseType.TEXT);
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain1", null, ChatContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testDashScopeStream() {
        setupMock(ProviderEnum.DASHSCOPE, TestDataReader.ResponseType.TEXT);
        ChatContext chatContext = new ChatContext(getStreamHandler());
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain2", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOllamaChat() {
        setupMock(ProviderEnum.OLLAMA, TestDataReader.ResponseType.TEXT);
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain3", null, ChatContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOllamaStream() {
        setupMock(ProviderEnum.OLLAMA, TestDataReader.ResponseType.TEXT);
        ChatContext chatContext = new ChatContext(getStreamHandler());
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain4", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOpenAIChat() {
        setupMock(ProviderEnum.OPENAI, TestDataReader.ResponseType.TEXT);
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain5", null, ChatContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOpenAIStream() {
        setupMock(ProviderEnum.OPENAI, TestDataReader.ResponseType.TEXT);
        ChatContext chatContext = new ChatContext(getStreamHandler());
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain6", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    private StreamHandler getStreamHandler() {
        return StreamHandler.builder()
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
                })
                .build();
    }

}
