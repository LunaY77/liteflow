package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;
import dev.langchain4j.model.chat.ChatModel;

/**
 * 分类组件的调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyAIInvocationHandler extends AbstractAIInvocationHandler<ClassifyProxyWrapBean> {

    public ClassifyAIInvocationHandler(ClassifyProxyWrapBean wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Object executeAIProcess(NodeComponent nodeComponent, Object[] args) {

        ChatModel chatModel = ModelFactory.getChatModel(wrapBean.getConfig());

        return null;
    }
}
