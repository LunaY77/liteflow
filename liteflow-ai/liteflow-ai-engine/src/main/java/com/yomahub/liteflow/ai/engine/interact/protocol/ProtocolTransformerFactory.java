package com.yomahub.liteflow.ai.engine.interact.protocol;

import cn.hutool.core.util.ServiceLoaderUtil;
import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.log.EngineLog;
import com.yomahub.liteflow.ai.engine.log.EngineLogManager;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息协议转换器工厂(spi注册)
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ProtocolTransformerFactory {

    private static final EngineLog LOG = EngineLogManager.getLogger(ProtocolTransformer.class);

    private static final Map<String, ProtocolTransformer> TRANSFORMER_REGISTRY;

    static {
        // spi 注册所有协议转换器
        TRANSFORMER_REGISTRY = ServiceLoaderUtil.loadList(ProtocolTransformer.class)
                .stream()
                .peek(t -> LOG.info("Discovered protocol transformer: {} for provider: {}",
                        t.getClass().getName(), t.getProviderName()))
                .collect(Collectors.toConcurrentMap(ProtocolTransformer::getProviderName, Function.identity()));
    }

    private ProtocolTransformerFactory() {}

    /**
     * 获取大模型协议转换器
     *
     * @param provider 模型提供者标识
     * @return 协议转换器实例
     */
    public static ProtocolTransformer getTransformer(String provider) {
        ProtocolTransformer protocolTransformer = TRANSFORMER_REGISTRY.get(provider);
        if (Objects.isNull(protocolTransformer)) {
            throw new LiteFlowAIEngineException("Unsupported protocol transformer: " + provider);
        }
        return protocolTransformer;
    }
}
