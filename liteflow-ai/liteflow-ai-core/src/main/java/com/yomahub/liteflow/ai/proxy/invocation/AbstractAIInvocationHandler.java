package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 抽象AI调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractAIInvocationHandler implements InvocationHandler {

    private final LFLog LOG = LFLoggerManager.getLogger(this.getClass());

    protected AIProxyWrapBean<?> wrapBean;

    public AbstractAIInvocationHandler(AIProxyWrapBean<?> wrapBean) {
        this.wrapBean = wrapBean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return executeAIProcess((NodeComponent) proxy, args);
    }

    /**
     * 执行AI节点处理逻辑
     *
     * @param nodeComponent AI节点组件
     * @param args
     * @return 处理结果
     */
    protected abstract Object executeAIProcess(NodeComponent nodeComponent, Object[] args);
}
