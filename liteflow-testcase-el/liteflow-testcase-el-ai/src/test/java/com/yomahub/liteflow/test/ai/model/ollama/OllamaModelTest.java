package com.yomahub.liteflow.test.ai.model.ollama;

import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.UserMessage;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatConfig;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatRequest;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaModelTest {

    private static final LFLog LOG = LFLoggerManager.getLogger(OllamaModelTest.class);

    @Test
    public void testBlocking() {
        ChatConfig config = OllamaChatConfig
                .builder()
                .apiUrl("http://localhost:11434/")
                .endPoint("/api/chat")
                .provider(OllamaConstant.PROVIDER_NAME)
                .model("qwen3:32b")
                .connectTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .readTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .build();

        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage("Why sky is blue?"));

        ChatOptions options = ChatOptions.DEFAULT;
        // 关闭 Thinking
        options.setEnableThinking(false);

        ChatRequest request = OllamaChatRequest.builder()
                .messages(messages)
                .options(options)
                .streaming(false)
                .transportType(TransportType.HTTP)
                .onStart(context -> LOG.info("chat start"))
                .onClose(context -> LOG.info("chat close"))
                .onCompletion((response, context) -> {
                    LOG.info("response: \n{}", response);
                    LOG.info("content: \n{}", response.getContent().getContent());
                    LOG.info("TokenUsage: \n{}", response.getTokenUsage());
                    LOG.info("FinishReason: \n{}", response.getFinishReason());
                    return response;
                })
                .build();

        // 请求体构建
        LOG.info("{}",
                request.toRequestBody()
                        .merge(config.toRequestBody())
        );

        System.out.println("=========================================");

        // 请求
        ChatModel chatModel = new OllamaChatModel(config);
        chatModel.chat(request);
    }

    @Test
    public void testStreaming() {
        ChatConfig config = OllamaChatConfig
                .builder()
                .apiUrl("http://localhost:11434/")
                .endPoint("/api/chat")
                .provider(OllamaConstant.PROVIDER_NAME)
                .model("qwen3:32b")
                .connectTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .readTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .build();

        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage("Why sky is blue?"));

        ChatOptions options = ChatOptions.DEFAULT;

        ChatRequest request = OllamaChatRequest.builder()
                .messages(messages)
                .options(options)
                .streaming(true)
                .transportType(TransportType.DnJson)
                .onStart(context -> LOG.info("chat start"))
                .onClose(context -> {
                    LOG.info("chat close");
                    System.exit(0);
                })
                .onThinking((text, context) -> {
                    LOG.info("chat thinking: {}", text);
                    return text;
                })
                .onText(((text, context) -> {
                    LOG.info("chat text: {}", text);
                    return text;
                }))
                .onCompletion((response, context) -> {
                    LOG.info("Thinking: {}", context.getAggregatedThinking());
                    LOG.info("Text: {}", context.getAggregatedText());
                    LOG.info("TokenUsage: {}", context.getTokenUsage());

                    LOG.info("response: \n{}", response);
                    return response;
                })
                .build();

        // 请求体构建
        LOG.info("{}",
                request.toRequestBody()
                        .merge(config.toRequestBody())
        );

        System.out.println("=========================================");

        // 请求
        ChatModel chatModel = new OllamaChatModel(config);
        chatModel.stream(request);
        while (true) {
        }
    }
}
