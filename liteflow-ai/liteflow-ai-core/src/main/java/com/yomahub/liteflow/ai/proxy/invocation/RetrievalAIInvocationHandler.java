package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.RetrievalProxyWrapBean;

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
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        return null;
    }
}
