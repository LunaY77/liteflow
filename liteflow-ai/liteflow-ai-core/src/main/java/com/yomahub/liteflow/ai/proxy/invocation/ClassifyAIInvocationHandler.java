package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;
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
    protected void checkValidation(ProcessorContext<ClassifyProxyWrapBean> processorContext) {
        // 调用父类的校验方法
        super.checkValidation(processorContext);
        // 校验分类类别
        if (SetUtil.isNotPresent(wrapBean.getCategories())) {
            throw new LiteFlowAIException("Categories cannot be empty for classification");
        }
        // 校验多标签分类
        if (!wrapBean.isMultiLabel() && wrapBean.getCategories().size() > 1) {
            throw new LiteFlowAIException("Multi-label classification is not allowed when multiLabel is false");
        }
        if (wrapBean.isMultiLabel() && wrapBean.getCategories().size() < 2) {
            throw new LiteFlowAIException("At least two categories are required for multi-label classification");
        }
    }

    @Override
    protected Object doExecuteAIProcess(ProcessorContext<ClassifyProxyWrapBean> processorContext, Object[] args) {

        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);

        return null;
    }
}
