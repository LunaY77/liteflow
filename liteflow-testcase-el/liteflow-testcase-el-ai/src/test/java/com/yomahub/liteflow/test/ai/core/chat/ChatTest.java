package com.yomahub.liteflow.test.ai.core.chat;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
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

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

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
                    System.out.println("Thinking: " + context.getAggregatedThinking());
                    System.out.println("Text: " + context.getAggregatedText());
                    System.out.println("Token Usage: " + response.getTokenUsage());

                    System.out.println("response: \n" + response.getContent().getContent());
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
