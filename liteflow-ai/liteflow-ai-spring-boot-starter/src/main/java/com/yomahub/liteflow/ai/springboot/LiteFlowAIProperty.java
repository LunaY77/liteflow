package com.yomahub.liteflow.ai.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * LiteFlow AI Spring Boot Starter 属性配置类
 *
 * @author 苍镜月
 * @since 2.16.0
 */
@ConfigurationProperties(prefix = "liteflow.ai")
public class LiteFlowAIProperty {

    private Boolean enable = true;

    private List<String> basePackages;

    private DashScopeConfig dashscope = new DashScopeConfig();

    private OpenAIConfig openai = new OpenAIConfig();

    private OllamaConfig ollama = new OllamaConfig();

    public Boolean isEnable() {
        return enable;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

    public DashScopeConfig getDashscope() {
        return dashscope;
    }

    public OpenAIConfig getOpenai() {
        return openai;
    }

    public OllamaConfig getOllama() {
        return ollama;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public void setDashscope(DashScopeConfig dashscope) {
        this.dashscope = dashscope;
    }

    public void setOpenai(OpenAIConfig openai) {
        this.openai = openai;
    }

    public void setOllama(OllamaConfig ollama) {
        this.ollama = ollama;
    }

    /**
     * DashScope 相关配置
     */
    public static class DashScopeConfig {
        private String apiKey;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }

    /**
     * OpenAI 相关配置
     */
    public static class OpenAIConfig {
        private String apiKey;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }

    /**
     * Ollama 相关配置
     */
    public static class OllamaConfig {
        private String apiKey;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }
}
