package com.yomahub.liteflow.ai.model.ollama.config;

import com.yomahub.liteflow.ai.model.ollama.model.OllamaModelProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Configuration
public class OllamaAutoConfiguration {

    @Bean
    public OllamaModelProvider ollamaModelProvider() {
        return new OllamaModelProvider();
    }
}
