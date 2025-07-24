package com.yomahub.liteflow.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * LiteFlow AI 参数配置
 *
 * @author 苍镜月
 * @since TODO
 */

@ConfigurationProperties(prefix = "liteflow.ai")
public class LiteFlowAIProperty {

    private boolean enable;

    private List<String> basePackages;

    public boolean isEnable() {
        return enable;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }
}
