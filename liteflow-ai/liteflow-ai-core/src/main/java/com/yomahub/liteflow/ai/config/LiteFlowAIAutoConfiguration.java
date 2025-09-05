package com.yomahub.liteflow.ai.config;

import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.parse.anno.ChatAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.anno.ClassifyAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.anno.WorkflowAnnotationProcessor;
import com.yomahub.liteflow.ai.proxy.AIComponentBeanPostProcessor;
import com.yomahub.liteflow.ai.proxy.AIComponentProxyRegistrar;
import com.yomahub.liteflow.ai.tool.SpringBeanToolRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * LiteFlow-AI 主配置
 *
 * @author 苍镜月
 * @since TODO
 */

@ConditionalOnProperty(prefix = "liteflow.ai", name = "enable", havingValue = "true")
public class LiteFlowAIAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ToolRegistry toolRegistry(ApplicationContext applicationContext) {
        return new SpringBeanToolRegistry(applicationContext);
    }

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

    @Bean
    public WorkflowAnnotationProcessor workflowAnnotationProcessor() {
        return new WorkflowAnnotationProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public LiteFlowAIModelPropertyRegistry liteFlowAIModelPropertyRegistry(
            @Autowired(required = false) List<LiteFlowAIModelProperty> modelPropertyList) {
        return new LiteFlowAIModelPropertyRegistry(modelPropertyList);
    }
}
