package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.annotation.AIClassify;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;

/**
 * 分类组件的调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyAIInvocationHandler extends AbstractAIInvocationHandler {

    public ClassifyAIInvocationHandler(AIProxyWrapBean<AIClassify> wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Object executeAIProcess(NodeComponent nodeComponent, Object[] args) {
        return null;
    }
}
