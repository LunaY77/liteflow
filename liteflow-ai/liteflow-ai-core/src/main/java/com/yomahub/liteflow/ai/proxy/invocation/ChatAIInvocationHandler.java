package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;

/**
 * 聊天组件的调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatAIInvocationHandler extends AbstractAIInvocationHandler {

    public ChatAIInvocationHandler(AIProxyWrapBean<AIChat> wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Object executeAIProcess(NodeComponent nodeComponent, Object[] args) {

        return null;
    }
}
