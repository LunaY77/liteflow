package com.yomahub.liteflow.ai.model.runtime;

import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.model.BaseModel;
import com.yomahub.liteflow.ai.model.ModelConfig;
import com.yomahub.liteflow.ai.util.SpringUtil;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模型运行时工厂
 *
 * @author 苍镜月
 * @since TODO
 */

public class ModelRuntimeFactory {

    private static final Map<String, Class<? extends BaseModel<? extends ModelConfig>>> PROVIDER_REGISTRY = new ConcurrentHashMap<>();
    private static final Map<String, BaseModel<? extends ModelConfig>> RUNTIME_CACHE = new ConcurrentHashMap<>();

    /**
     * 注册运行时实现
     *
     * @param provider      模型提供者标识
     * @param providerClass 模型提供者类
     */
    public static void registerModelProvider(String provider, Class<? extends BaseModel<? extends ModelConfig>> providerClass) {
        PROVIDER_REGISTRY.put(provider, providerClass);
    }

    /**
     * 创建运行时实例
     *
     * @param provider 模型提供者标识
     * @param config   模型配置
     * @return 模型运行时实例
     */
    public static BaseModel<? extends ModelConfig> createRuntime(String provider, ModelConfig config) {
        String cacheKey = provider + "_" + config.hashCode();

        return RUNTIME_CACHE.computeIfAbsent(cacheKey, key -> {
            Class<? extends BaseModel<? extends ModelConfig>> runtimeClass = PROVIDER_REGISTRY.get(provider);
            if (Objects.isNull(runtimeClass)) {
                throw new LiteFlowAIException("不支持的模型提供者: " + provider);
            }

            try {
                return SpringUtil.getBean(runtimeClass, config);
            } catch (Exception e) {
                throw new RuntimeException("创建模型运行时失败, provider: " + provider + ", modelConfig: " + config, e);
            }
        });
    }

    /**
     * 获取已注册的模型提供者列表
     *
     * @return 已注册的模型提供者集合
     */
    public static Set<String> getSupportedProviders() {
        return PROVIDER_REGISTRY.keySet();
    }

}
