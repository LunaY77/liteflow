package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.proxy.wrap.RetrievalProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;

/**
 * RAG组件的调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class RetrievalAIInvocationHandler extends AbstractAIInvocationHandler<RetrievalProxyWrapBean> {

    public RetrievalAIInvocationHandler(RetrievalProxyWrapBean wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Object executeAIProcess(NodeComponent nodeComponent, Object[] args) {
        return null;
    }
}
