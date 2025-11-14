package com.yomahub.liteflow.test.ai.model.structure.ollama;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.MessageType;
import com.yomahub.liteflow.ai.engine.model.chat.message.SystemMessage;
import com.yomahub.liteflow.ai.engine.model.chat.message.UserMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatRequest;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockInteractClient;
import com.yomahub.liteflow.test.ai.model.structure.output.MathReasoning;
import com.yomahub.liteflow.test.ai.model.util.StreamUtil;
import io.reactivex.rxjava3.core.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Ollama 结构化输出 测试
 * <p>
 * 请注意！！！在测试代码中，构建的request仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故请求中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class OllamaStructureTest extends MockAITest {

    OllamaChatModel chatModel;
    OllamaChatRequest.Builder chatRequestBuilder;

    @Test
    public void testStructure() {
        setupChatMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.BLOCKING_STRUCTURED);

        List<Message> messages = Arrays.asList(
                new SystemMessage("你是一位数学辅导老师"),
                new UserMessage("使用中文解题: 8x + 9 = 32 and x + y = 1"));

        ChatResponse response = chatModel.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        // 结构化输出相关配置
                        .responseType(ResponseType.JSON)
                        .targetType(MathReasoning.class)
                        // 结构化输出相关配置
                        .build());

        // 将响应转换为结构化结果对象
        MathReasoning result = response.as(MathReasoning.class);

        Assertions.assertNotNull(result);

        System.out.println(result);

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
    }

    @Test
    public void testStructureStreaming() {
        setupChatMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.STREAMING_STRUCTURED);

        List<Message> messages = Arrays.asList(
                new SystemMessage("你是一位数学辅导老师"),
                new UserMessage("使用中文解题: 8x + 9 = 32 and x + y = 1"));

        Flowable<ChunkEvent> stream = chatModel.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.DN_JSON)
                        .messages(messages)
                        // 结构化输出相关配置
                        .responseType(ResponseType.JSON)
                        .targetType(MathReasoning.class)
                        // 结构化输出相关配置
                        .build());

        ChatResponse response = stream.doOnNext(StreamUtil.getChunkEventConsumer())
                .blockingLast()
                .getFinalResponse();

        MathReasoning result = response.as(MathReasoning.class);

        Assertions.assertNotNull(result);

        System.out.println(result);

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

        chatRequestBuilder = OllamaChatRequest.builder();
    }
}
