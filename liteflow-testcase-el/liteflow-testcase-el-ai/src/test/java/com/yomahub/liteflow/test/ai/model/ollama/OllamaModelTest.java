package com.yomahub.liteflow.test.ai.model.ollama;

import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatConfig;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatRequest;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaModelTest {

    private static final LFLog LOG = LFLoggerManager.getLogger(OllamaModelTest.class);

    @Test
    public void testOllamaChatModel() {
        OllamaChatConfig config = OllamaChatConfig
                .builder()
                .apiUrl("http://localhost:11434/")
                .endPoint("/api/generate")
                .provider(OllamaConstant.PROVIDER_NAME)
                .model("qwen3:32b")
                .streaming(true)
                .timeout(Duration.of(10, ChronoUnit.MINUTES))
                .transportType(TransportType.DnJson)
                .build();

        ChatRequest request = OllamaChatRequest.builder()
                .prompt("Why sky is blue?")
                .options(ChatOptions.DEFAULT)
                .onStart(context -> LOG.info("chat start"))
                .onClose(context -> LOG.info("chat close"))
//                .onCompletion(((response, context) -> {
//                    LOG.info("chat completion: \n{}", response.getMessage().getContent());
//                    AssistantMessage modifiedMessage = new AssistantMessage(
//                            response.getMessage().getContent() + " \n(modified by onCompletion)"
//                    );
//                    response.setMessage(modifiedMessage);
//                    return response;
//                }))
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
        while (true) {}
    }
}
