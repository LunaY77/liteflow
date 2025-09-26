package com.yomahub.liteflow.ai.model.openai.config;

import com.yomahub.liteflow.ai.model.openai.model.OpenAIModelProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAI 模型自动配置类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Configuration
@EnableConfigurationProperties(OpenAIModelProperty.class)
public class OpenAIAutoConfiguration {

    @Bean
    public OpenAIModelProvider openAIModelProvider() {
        return new OpenAIModelProvider();
    }
}
