package com.yomahub.liteflow.ai.parse;

import com.yomahub.liteflow.ai.annotation.AIInput;
import com.yomahub.liteflow.ai.annotation.AIOutput;
import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;

/**
 * 注解解析处理器上下文
 *
 * @author 苍镜月
 * @since TODO
 */

public class ProcessorContext<T extends AIProxyWrapBean<?>> {

    private final T wrapBean;
    private final ChatContext chatContext;
    private final NodeComponent nodeComponent;
    private final AIInput aiInputAnno;
    private final AIOutput aiOutputAnno;

    public ProcessorContext(T wrapBean, ChatContext chatContext, NodeComponent nodeComponent) {
        this.wrapBean = wrapBean;
        this.chatContext = chatContext;
        this.nodeComponent = nodeComponent;
        this.aiInputAnno = wrapBean.getInterfaceClass().getAnnotation(AIInput.class);
        this.aiOutputAnno = wrapBean.getInterfaceClass().getAnnotation(AIOutput.class);
    }

    public T getWrapBean() {
        return wrapBean;
    }

    public ChatContext getChatContext() {
        return chatContext;
    }

    public NodeComponent getNodeComponent() {
        return nodeComponent;
    }

    public AIInput getAiInputAnno() {
        return aiInputAnno;
    }

    public AIOutput getAiOutputAnno() {
        return aiOutputAnno;
    }
}
