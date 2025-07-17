package com.yomahub.liteflow.ai.interact.protocol;

import com.yomahub.liteflow.ai.exception.LiteFlowAIException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息协议转换器工厂
 *
 * @author 苍镜月
 * @since TODO
 */

public class ProtocolTransformerFactory {

    private static final Map<String, ProtocolTransformer> TRANSFORMER_REGISTRY = new ConcurrentHashMap<>();

    /**
     * 注册大模型协议转换器
     *
     * @param provider    模型提供者标识
     * @param transformer 协议转换器实例
     */
    public static void registerTransformer(String provider, ProtocolTransformer transformer) {
        TRANSFORMER_REGISTRY.put(provider, transformer);
    }

    /**
     * 获取大模型协议转换器
     *
     * @param provider 模型提供者标识
     * @return 协议转换器实例
     */
    public static ProtocolTransformer getTransformer(String provider) {
        ProtocolTransformer protocolTransformer = TRANSFORMER_REGISTRY.get(provider);
        if (Objects.isNull(protocolTransformer)) {
            throw new LiteFlowAIException("不支持的协议转换器: " + provider);
        }
        return protocolTransformer;
    }
}
