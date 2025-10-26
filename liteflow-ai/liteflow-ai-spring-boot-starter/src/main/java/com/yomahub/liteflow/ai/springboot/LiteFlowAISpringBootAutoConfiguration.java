package com.yomahub.liteflow.ai.springboot;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.config.LiteFlowAIAutoConfiguration;
import com.yomahub.liteflow.ai.config.LiteFlowAIConfig;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

/**
 * LiteFlow-AI SpringBoot 自动配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Configuration
@ConditionalOnProperty(prefix = "liteflow.ai", name = "enable", havingValue = "true")
@EnableConfigurationProperties(LiteFlowAIProperty.class)
@Import(LiteFlowAIAutoConfiguration.class)
public class LiteFlowAISpringBootAutoConfiguration {

    /**
     * LiteFlowAIConfig Bean
     *
     * @param property SpringBoot 配置
     * @return LiteFlowAIConfig 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public LiteFlowAIConfig liteFlowAIConfig(LiteFlowAIProperty property) {
        LiteFlowAIConfig config = new LiteFlowAIConfig();
        config.setEnable(property.isEnable());
        config.setBasePackages(property.getBasePackages());

        Map<String, LiteFlowAIConfig.ModelProviderConfig> providers = new HashMap<>();

        // DashScope
        if (property.getDashscope() != null && StrUtil.isNotBlank(property.getDashscope().getApiKey())) {
            LiteFlowAIConfig.ModelProviderConfig dashscopeConfig = new LiteFlowAIConfig.ModelProviderConfig();
            dashscopeConfig.setApiKey(property.getDashscope().getApiKey());
            providers.put(ProviderEnum.DASHSCOPE.getProviderName(), dashscopeConfig);
        }

        // OpenAI
        if (property.getOpenai() != null && StrUtil.isNotBlank(property.getOpenai().getApiKey())) {
            LiteFlowAIConfig.ModelProviderConfig openaiConfig = new LiteFlowAIConfig.ModelProviderConfig();
            openaiConfig.setApiKey(property.getOpenai().getApiKey());
            providers.put(ProviderEnum.OPENAI.getProviderName(), openaiConfig);
        }

        // Ollama
        if (property.getOllama() != null && StrUtil.isNotBlank(property.getOllama().getApiKey())) {
            LiteFlowAIConfig.ModelProviderConfig ollamaConfig = new LiteFlowAIConfig.ModelProviderConfig();
            ollamaConfig.setApiKey(property.getOllama().getApiKey());
            providers.put(ProviderEnum.OLLAMA.getProviderName(), ollamaConfig);
        }

        config.setProviders(providers);

        return config;
    }
}
