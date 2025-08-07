package com.yomahub.liteflow.test.ai.core.proxy;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.slot.DefaultContext;
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

@TestPropertySource(properties = {"spring.config.location=classpath:core/proxy/application.yaml"})
@SpringBootTest(classes = {ChatTest.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.core.proxy.cmp"})
public class ChatTest {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testBlockingChat() {
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain1", null, ChatContext.class, DefaultContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testStreaming() {
        StreamHandler streamHandler = new StreamHandler() {

            @Override
            public void onStart(InteractContext context) {
                System.out.println("chat start");
            }

            @Override
            public void onClose(InteractContext context) {
                System.out.println("chat close");
            }

            @Override
            public void onError(InteractContext context, Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public String onText(String content, InteractContext context) {
                System.out.println("Received text: " + content);
                return content;
            }

            @Override
            public String onThinking(String content, InteractContext context) {
                System.out.println("Received thinking: " + content);
                return content;
            }

            @Override
            public Object onToolsCalling(Object content, InteractContext context) {
                return content;
            }

            @Override
            public TokenUsage onUsage(Object content, InteractContext context) {
                return null;
            }

            @Override
            public Object onGrounding(Object content, InteractContext context) {
                return null;
            }

            @Override
            public ChatResponse onCompletion(ChatResponse response, InteractContext context) {
                System.out.println("Thinking: " + context.getAggregatedThinking());
                System.out.println("Text: " + context.getAggregatedText());
                System.out.println("Token Usage: " + response.getTokenUsage());

                System.out.println("response: \n" + response);
                return response;
            }

            @Override
            public ChatResponse onFinal(ChatResponse response, InteractContext context) {
                return response;
            }
        };

        ChatContext chatContext = new ChatContext(streamHandler);

        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain2", null, chatContext, DefaultContext.class);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }
}
