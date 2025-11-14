package com.yomahub.liteflow.test.ai.model.chat.openai;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.MessageType;
import com.yomahub.liteflow.ai.engine.model.chat.message.SystemMessage;
import com.yomahub.liteflow.ai.engine.model.chat.message.UserMessage;
import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatModel;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatRequest;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockInteractClient;
import com.yomahub.liteflow.test.ai.model.util.StreamUtil;
import io.reactivex.rxjava3.core.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * OpenAI chat 测试
 * <p>
 * 请注意！！！在测试代码中，构建的request仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故请求中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class OpenAIChatTest extends MockAITest {

    OpenAIChatModel chatModel;
    OpenAIChatRequest.Builder chatRequestBuilder;

    @Test
    public void testBlocking() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.BLOCKING_TEXT);

        List<Message> messages = Arrays.asList(
                new SystemMessage("You are a helpful assistant."),
                new UserMessage("请解释一下什么是“人工智能”"));

        ChatResponse response = chatModel.chat(
                chatRequestBuilder
                        .streaming(false)
                        .transportType(TransportType.HTTP)
                        .messages(messages)
                        .build());

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
    }

    @Test
    public void testStreaming() {
        setupChatMock(ProviderEnum.OPENAI, TestDataReader.RequestType.STREAMING_TEXT);

        List<Message> messages = Arrays.asList(
                new SystemMessage("You are a helpful assistant."),
                new UserMessage("请给我讲一个关于未来城市的短故事"));

        Flowable<ChunkEvent> stream = Flowable.fromPublisher(chatModel.stream(
                chatRequestBuilder
                        .streaming(true)
                        .transportType(TransportType.SSE)
                        .messages(messages)
                        .build()
        ));

        ChatResponse response = stream.doOnNext(StreamUtil.getChunkEventConsumer())
                .blockingLast()
                .getFinalResponse();

        Assertions.assertEquals(FinishReason.STOP, response.getFinishReason());
        Assertions.assertEquals(MessageType.ASSISTANT, response.getOutput().getMessageType());
    }

    @BeforeEach
    public void setup() {
        chatModel = OpenAIChatModel.builder()
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
