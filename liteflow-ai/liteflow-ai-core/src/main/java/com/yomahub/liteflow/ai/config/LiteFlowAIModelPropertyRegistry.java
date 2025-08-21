package com.yomahub.liteflow.ai.config;

import cn.hutool.core.collection.CollectionUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * AI 模型属性注册中心，用于注册和管理 AI 模型属性
 *
 * @author 苍镜月
 * @since TODO
 */

public class LiteFlowAIModelPropertyRegistry {

    private final Map<String, LiteFlowAIModelProperty> propertyMap;

    /**
     * 构造函数，接受一个 AI 模型属性列表，并将其转换为一个 Map
     *
     * @param propertyList AI 模型属性列表
     */
    public LiteFlowAIModelPropertyRegistry(List<LiteFlowAIModelProperty> propertyList) {
        if (CollectionUtil.isEmpty(propertyList)) {
            this.propertyMap = Collections.emptyMap();
        } else {
            this.propertyMap = propertyList.stream()
                    .collect(Collectors.toMap(
                            LiteFlowAIModelProperty::getProviderName,
                            Function.identity(),
                            (p1, p2) -> p2)
                    );
        }
    }

    /**
     * 根据模型厂商名称获取其完整的属性配置对象。
     *
     * @param providerName 模型厂商名称
     * @return 对应的属性配置对象的 Optional 包装。
     */
    public Optional<LiteFlowAIModelProperty> getProviderProperties(String providerName) {
        return Optional.ofNullable(propertyMap.get(providerName));
    }

    /**
     * 根据模型厂商名称直接获取其 API Key。
     *
     * @param providerName 模型厂商名称
     * @return API Key 的 Optional 包装。
     */
    public Optional<String> getApiKey(String providerName) {
        return getProviderProperties(providerName).map(LiteFlowAIModelProperty::getApiKey);
    }
}
