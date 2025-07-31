package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
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
    protected Object doExecuteAIProcess(ProcessorContext<ClassifyProxyWrapBean> processorContext, Object[] args) {

        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);

        return null;
    }
}
