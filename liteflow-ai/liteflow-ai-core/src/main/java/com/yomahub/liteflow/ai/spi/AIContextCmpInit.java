package com.yomahub.liteflow.ai.spi;

import com.yomahub.liteflow.ai.proxy.holder.AIComponentHolder;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.process.holder.SpringNodeIdHolder;
import com.yomahub.liteflow.spi.ContextCmpInit;

/**
 * LiteFlow-AI 环境容器上下文组件初始化实现
 *
 * @author 苍镜月
 * @since TODO
 */

public class AIContextCmpInit implements ContextCmpInit {
    @Override
    public void initCmp() {
        SpringNodeIdHolder.getNodeIdSet().forEach(FlowBus::addManagedNode);
        // 注册AI组件到FlowBus(LiteFlow容器)
        AIComponentHolder.registerToFlowBus();
    }

    @Override
    public int priority() {
        return 0;
    }
}
