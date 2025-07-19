package com.yomahub.liteflow.test.ai.model.ollama;

import com.yomahub.liteflow.ai.interact.transport.TransportType;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import com.yomahub.liteflow.ai.model.ollama.interact.OllamaProtocolTransformer;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Configuration
public class ModelConfiguration {

    @Bean
    public OllamaChatConfig ollamaChatConfig() {
        return OllamaChatConfig
                .builder()
                .apiUrl("http://localhost:11434/")
                .endPoint("/api/generate")
                .provider(OllamaConstant.PROVIDER_NAME)
                .model("qwen3:32b")
                .streaming(false)
                .transportType(TransportType.HTTP)
                .build();
    }
//
//    @Bean
//    public OllamaChatModel ollamaChatModel(OllamaChatConfig config) {
//        return new OllamaChatModel(config);
//    }

    @Bean
    public OllamaProtocolTransformer ollamaProtocolTransformer() {
        return new OllamaProtocolTransformer();
    }
}
