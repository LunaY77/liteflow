package com.yomahub.liteflow.test.ai.core.chat;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

@TestPropertySource(properties = {"spring.config.location=classpath:core/chat/application.yaml"})
@SpringBootTest(classes = {ChatTest.class, SpringUtil.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.core.chat.cmp"})
public class ChatTest {

    @Resource
    private FlowExecutor flowExecutor;

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

    @Test
    public void testDashScopeChat() {
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain1", null, ChatContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testDashScopeStream() {
        ChatContext chatContext = new ChatContext(getStreamHandler());
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain2", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }


    @Test
    public void testOllamaChat() {
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain3", null, ChatContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOllamaStream() {
        ChatContext chatContext = new ChatContext(getStreamHandler());
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain4", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOpenAIChat() {
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain5", null, ChatContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOpenAIStream() {
        ChatContext chatContext = new ChatContext(getStreamHandler());
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain6", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }
}
