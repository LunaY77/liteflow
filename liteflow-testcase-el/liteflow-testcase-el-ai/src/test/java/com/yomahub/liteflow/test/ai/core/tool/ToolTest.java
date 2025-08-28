package com.yomahub.liteflow.test.ai.core.tool;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.test.ai.core.tool.tools.ToolConfig;
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

@TestPropertySource(properties = {"spring.config.location=classpath:core/tool/application.yaml"})
@SpringBootTest(classes = {ToolTest.class, ToolConfig.class, SpringUtil.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.core.tool.cmp"})
public class ToolTest {

    @Resource
    private FlowExecutor flowExecutor;

    @Resource
    private ToolRegistry toolRegistry;

    @Test
    public void testToolRegistry() {
        Assertions.assertNotNull(toolRegistry);
        Assertions.assertFalse(toolRegistry.getAllTools().isEmpty(), "Tool registry should contain tools");
        Assertions.assertEquals(4, toolRegistry.getAllTools().size());
        Assertions.assertEquals("weather_tool", toolRegistry.getTool("weather_tool").getName());
        Assertions.assertEquals("assemble_tool", toolRegistry.getTool("assemble_tool").getName());
        ToolCallBack testTool = toolRegistry.getTool("test_tool");
        String res = testTool.call("{\"arg0\": \"Hello\", \"arg1\": \"World\"}");
        System.out.println(res);
        String nullRes = toolRegistry.getTool("null_tool").call(null);
        System.out.println(nullRes);
    }

    @Test
    public void testBlockingChatWithTool() {
        ChatContext chatContext = ChatContext.builder()
                .streamHandler(StreamHandler.builder()
                        .onCompletion((response, context) -> {
                            System.out.println("response content: " + response.getOutput().getContent());
                            System.out.println("Token Usage: " + response.getTokenUsage());
                            System.out.println("Finish Reason: " + response.getFinishReason());
                            return response;
                        })
                        .build())
                .build();
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain1", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testStreaming() {
        ChatContext chatContext = ChatContext.builder()
                .streamHandler(StreamHandler.builder()
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

                            System.out.println("response: \n" + response);
                            return response;
                        })
                        .build())
                .build();

        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain2", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }
}
