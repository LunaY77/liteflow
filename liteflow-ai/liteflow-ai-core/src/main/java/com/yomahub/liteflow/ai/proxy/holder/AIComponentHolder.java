package com.yomahub.liteflow.ai.proxy.holder;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.FlowBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI 组件持有器
 * {@link  com.yomahub.liteflow.ai.spi.AIContextCmpInit} 在此将 AI 组件注册到 LiteFlow 容器中
 *
 * @author 苍镜月
 * @since TODO
 */

public class AIComponentHolder {

    private static final Map<String, NodeComponent> aiComponentMap = new ConcurrentHashMap<>();

    /**
     * 添加AI组件到持有器
     *
     * @param nodeId       节点ID
     * @param aiComponent  AI组件实例
     */
    public static void addAIComponent(String nodeId, NodeComponent aiComponent) {
        aiComponentMap.put(nodeId, aiComponent);
    }

    /**
     * 注册到FlowBus
     */
    public static void registerToFlowBus() {
        // 注册到FlowBus
        aiComponentMap.forEach(FlowBus::addManagedNode);
    }
}
