package com.yomahub.liteflow.ai.config;

import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.parse.anno.ChatAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.anno.ClassifyAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.anno.WorkflowAnnotationProcessor;
import com.yomahub.liteflow.ai.proxy.AIComponentBeanPostProcessor;
import com.yomahub.liteflow.ai.proxy.AIComponentProxyRegistrar;
import com.yomahub.liteflow.ai.tool.SpringBeanToolRegistry;
import com.yomahub.liteflow.ai.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LiteFlow-AI 主配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Configuration
public class LiteFlowAIAutoConfiguration {

    private LiteFlowAIConfig liteFlowAIConfig;

    public LiteFlowAIAutoConfiguration() {
    }

    @Autowired(required = false)
    public void setLiteFlowAIConfig(LiteFlowAIConfig liteFlowAIConfig) {
        this.liteFlowAIConfig = liteFlowAIConfig;
        LiteFlowAIConfigGetter.setLiteFlowAIConfig(liteFlowAIConfig);
    }

    @Bean
    public ToolRegistry toolRegistry(ApplicationContext applicationContext) {
        return new SpringBeanToolRegistry(applicationContext);
    }

    @Bean
    public StreamHandler streamHandler() {
        return StreamHandler.builder().build();
    }

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    public AIComponentProxyRegistrar aiComponentProxyRegistrar() {
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

    @Bean
    public WorkflowAnnotationProcessor workflowAnnotationProcessor() {
        return new WorkflowAnnotationProcessor();
    }
}
