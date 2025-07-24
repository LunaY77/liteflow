package com.yomahub.liteflow.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import static com.yomahub.liteflow.ai.util.SetUtil.setIfPresent;

/**
 * LiteFlow-AI 参数配置
 *
 * @author 苍镜月
 * @since TODO
 */

@EnableConfigurationProperties({ LiteFlowAIProperty.class })
@PropertySource(name = "Liteflow AI Default Properties", value = "classpath:/META-INF/liteflow-ai-default.properties")
public class LiteFlowAIPropertyAutoConfiguration {

    @Bean
    public LiteFlowAIConfig liteFlowAIConfig(LiteFlowAIProperty liteFlowAIProperty) {
        LiteFlowAIConfig liteFlowAIConfig = new LiteFlowAIConfig();
        setIfPresent(liteFlowAIConfig::setEnable, liteFlowAIProperty.isEnable());
        setIfPresent(liteFlowAIConfig::setBasePackages, liteFlowAIProperty.getBasePackages());
        return liteFlowAIConfig;
    }
}
