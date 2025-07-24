package com.yomahub.liteflow.ai.config;

import java.util.List;

/**
 * LiteFlow AI 配置类
 *
 * @author 苍镜月
 * @since TODO
 */

public class LiteFlowAIConfig {

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
