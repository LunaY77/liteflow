package com.yomahub.liteflow.ai.config;

/**
 * AI 模型属性接口，用于在配置文件中配置apikey等敏感信息
 *
 * @author 苍镜月
 * @since TODO
 */

public interface LiteFlowAIModelProperty {

    /**
     * 获取 AI 模型提供者名称
     *
     * @return AI 模型提供者名称
     */
    String getProviderName();

    /**
     * 获取 API Key
     *
     * @return API Key
     */
    String getApiKey();
}
