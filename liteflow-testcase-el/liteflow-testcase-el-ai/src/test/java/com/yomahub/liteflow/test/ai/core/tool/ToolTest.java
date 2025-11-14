package com.yomahub.liteflow.test.ai.core.tool;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.chunk.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.test.ai.core.tool.tools.ToolConfig;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;


@TestPropertySource(properties = {"spring.config.location=classpath:core/tool/application.yaml"})
@SpringBootTest(classes = {ToolTest.class, ToolConfig.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.core.tool.cmp"})
public class ToolTest extends MockAITest {

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
    public void testOpenAIBlockingChatWithTool() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_TOOL_CALL, TestDataReader.RequestType.BLOCKING_TOOL_CALL_2);
        ChatContext chatContext = buildBlockingChatContext();
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain1", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOpenAIStreamingChatWithTool() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TOOL_CALL, TestDataReader.RequestType.STREAMING_TOOL_CALL_2);
        ChatContext chatContext = buildStreamingChatContext();
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain2", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testDashScopeBlockingChatWithTool() {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.BLOCKING_TOOL_CALL, TestDataReader.RequestType.BLOCKING_TOOL_CALL_2);
        ChatContext chatContext = buildBlockingChatContext();
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain3", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testDashScopeStreamingChatWithTool() {
        setupChatMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.STREAMING_TOOL_CALL, TestDataReader.RequestType.STREAMING_TOOL_CALL_2);
        ChatContext chatContext = buildStreamingChatContext();
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain4", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOllamaBlockingChatWithTool() {
        setupChatMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.BLOCKING_TOOL_CALL, TestDataReader.RequestType.BLOCKING_TOOL_CALL_2);
        ChatContext chatContext = buildBlockingChatContext();
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain5", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    @Test
    public void testOllamaStreamingChatWithTool() {
        setupChatMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.STREAMING_TOOL_CALL, TestDataReader.RequestType.STREAMING_TOOL_CALL_2);
        ChatContext chatContext = buildStreamingChatContext();
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain6", null, chatContext);
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }

    private ChatContext buildBlockingChatContext() {
        return ChatContext.builder()
                .build();
    }

    private ChatContext buildStreamingChatContext() {
        return ChatContext.builder()
                .streamHandler(getStreamHandler())
                .build();
    }

    private StreamHandler getStreamHandler() {
        return eventStream -> eventStream.doOnNext(chunkEvent -> {
            if (chunkEvent.isStart()) {
                System.out.println("chat start");
            }
            if (chunkEvent.isChunk()) {
                StreamingProtocolChunk chunk = chunkEvent.getTransformedChunk();
                switch (chunk.getType()) {
                    case TEXT:
                        System.out.println(chunk.getData());
                        break;
                    case THINKING:
                        System.out.println("[Thinking] " + chunk.getData());
                }
            }
            if (chunkEvent.isComplete()) {
                ChatResponse response = chunkEvent.getFinalResponse();
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
                System.out.println("chat close");
            }
        });
    }
}
