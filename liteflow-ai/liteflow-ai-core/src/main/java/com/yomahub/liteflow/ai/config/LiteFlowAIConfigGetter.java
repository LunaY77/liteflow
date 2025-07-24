package com.yomahub.liteflow.ai.config;

import com.yomahub.liteflow.spi.holder.ContextAwareHolder;

import java.util.Objects;

/**
 * LiteFlow-AI 配置获取器
 *
 * @author 苍镜月
 * @since TODO
 */

public class LiteFlowAIConfigGetter {

    private static LiteFlowAIConfig liteFlowAIConfig;

    public static LiteFlowAIConfig get() {
        if (Objects.isNull(liteFlowAIConfig)) {
            liteFlowAIConfig = ContextAwareHolder.loadContextAware().getBean(LiteFlowAIConfig.class);
            if (Objects.isNull(liteFlowAIConfig)) {
                liteFlowAIConfig = new LiteFlowAIConfig();
            }
        }
        return liteFlowAIConfig;
    }

    public static void clean() {
        liteFlowAIConfig = null;
    }

    public static void setLiteFlowAIConfig(LiteFlowAIConfig liteFlowAIConfig) {
        LiteFlowAIConfigGetter.liteFlowAIConfig = liteFlowAIConfig;
    }
}
