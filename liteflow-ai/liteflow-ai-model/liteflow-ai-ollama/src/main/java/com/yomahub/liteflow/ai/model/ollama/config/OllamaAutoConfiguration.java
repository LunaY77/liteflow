package com.yomahub.liteflow.ai.model.ollama.config;

import com.yomahub.liteflow.ai.model.ollama.model.OllamaModelProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ollama 模型自动配置类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Configuration
public class OllamaAutoConfiguration {

    @Bean
    public OllamaModelProvider ollamaModelProvider() {
        return new OllamaModelProvider();
    }
}
