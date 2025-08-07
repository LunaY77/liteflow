package com.yomahub.liteflow.ai.config;

import com.yomahub.liteflow.ai.parse.anno.ChatAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.anno.ClassifyAnnotationProcessor;
import com.yomahub.liteflow.ai.proxy.AIComponentBeanPostProcessor;
import com.yomahub.liteflow.ai.proxy.AIComponentProxyRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * LiteFlow-AI 主配置
 *
 * @author 苍镜月
 * @since TODO
 */

@ConditionalOnProperty(prefix = "liteflow.ai", name = "enable", havingValue = "true")
public class LiteFlowAIAutoConfiguration {

    @Bean
    public static AIComponentProxyRegistrar aiComponentProxyRegistrar() {
        return new AIComponentProxyRegistrar();
    }

    @Bean
    public AIComponentBeanPostProcessor aiComponentPostProcessor() {
        return new AIComponentBeanPostProcessor();
    }

    @Bean
    public ChatAnnotationProcessor chatAnnotationProcessor() {
        return new ChatAnnotationProcessor();
    }

    @Bean
    public ClassifyAnnotationProcessor classifyAnnotationProcessor() {
        return new ClassifyAnnotationProcessor();
    }

//    @Bean
//    public RetrievalAnnotationProcessor retrievalAnnotationProcessor() {
//        return new RetrievalAnnotationProcessor();
//    }

}
