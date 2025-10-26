package com.yomahub.liteflow.ai.config;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.*;

/**
 * LiteFlow-AI 参数配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Configuration
@PropertySource(name = "Liteflow AI Default Properties", value = "classpath:/META-INF/liteflow-ai-default.properties")
public class LiteFlowAIPropertyAutoConfiguration {

    @Value("${liteflow.ai.enable:true}")
    private Boolean enable;

    @Value("${liteflow.ai.base-packages:}")
    private String basePackages;

    @Value("${liteflow.ai.dashscope.api-key:}")
    private String dashscopeApiKey;

    @Value("${liteflow.ai.openai.api-key:}")
    private String openaiApiKey;

    @Value("${liteflow.ai.ollama.api-key:}")
    private String ollamaApiKey;

    @Bean
    public LiteFlowAIConfig liteFlowAIConfig() {
        LiteFlowAIConfig config = new LiteFlowAIConfig();
        config.setEnable(Boolean.TRUE.equals(enable));

        // 解析 basePackages
        if (Objects.nonNull(basePackages) && !basePackages.trim().isEmpty()) {
            List<String> packageList = Arrays.asList(basePackages.split(","));
            config.setBasePackages(packageList);
        }

        // 设置模型提供商配置
        Map<String, LiteFlowAIConfig.ModelProviderConfig> providers = new HashMap<>();

        // DashScope
        if (StrUtil.isNotBlank(dashscopeApiKey)) {
            LiteFlowAIConfig.ModelProviderConfig dashscopeConfig = new LiteFlowAIConfig.ModelProviderConfig();
            dashscopeConfig.setApiKey(dashscopeApiKey);
            providers.put(ProviderEnum.DASHSCOPE.getProviderName(), dashscopeConfig);
        }

        // OpenAI
        if (StrUtil.isNotBlank(openaiApiKey)) {
            LiteFlowAIConfig.ModelProviderConfig openaiConfig = new LiteFlowAIConfig.ModelProviderConfig();
            openaiConfig.setApiKey(openaiApiKey);
            providers.put(ProviderEnum.OPENAI.getProviderName(), openaiConfig);
        }

        // Ollama
        if (StrUtil.isNotBlank(ollamaApiKey)) {
            LiteFlowAIConfig.ModelProviderConfig ollamaConfig = new LiteFlowAIConfig.ModelProviderConfig();
            ollamaConfig.setApiKey(ollamaApiKey);
            providers.put(ProviderEnum.OLLAMA.getProviderName(), ollamaConfig);
        }

        config.setProviders(providers);

        return config;
    }
}
