package com.yomahub.liteflow.ai.ollama.config;

import com.yomahub.liteflow.ai.ollama.model.OllamaModelProvider;
import com.yomahub.liteflow.springboot.config.LiteflowMainAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ollama 自动配置
 *
 * @author 苍镜月
 * @since TODO
 */

@Configuration
@AutoConfigureBefore({ LiteflowMainAutoConfiguration.class })
public class OllamaAutoConfiguration {

    @Bean
    public OllamaModelProvider ollamaModelProvider() {
        return new OllamaModelProvider();
    }
}
