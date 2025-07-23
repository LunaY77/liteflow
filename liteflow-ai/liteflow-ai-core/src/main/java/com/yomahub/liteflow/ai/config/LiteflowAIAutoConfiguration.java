package com.yomahub.liteflow.ai.config;

import com.yomahub.liteflow.ai.proxy.AIComponentPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Configuration
public class LiteflowAIAutoConfiguration {

    @Bean
    public AIComponentPostProcessor aiComponentPostProcessor() {
        return new AIComponentPostProcessor();
    }
}
