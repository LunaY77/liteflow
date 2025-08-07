package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.domain.dto.ParsedClassifyAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;

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
    protected void checkValidation(ProcessorContext<?> processorContext) {
        // 调用父类的校验方法
        super.checkValidation(processorContext);
        ParsedClassifyAnnotationConfig annotationConfig = (ParsedClassifyAnnotationConfig) processorContext.getParsedAnnotationConfig();
        // 校验分类类别
        if (SetUtil.isNotPresent(annotationConfig.getCategories())) {
            throw new LiteFlowAIException("Categories cannot be empty for classification");
        }
        // 校验多标签分类
        if (!annotationConfig.isMultiLabel() && annotationConfig.getCategories().size() > 1) {
            throw new LiteFlowAIException("Multi-label classification is not allowed when multiLabel is false");
        }
        if (annotationConfig.isMultiLabel() && annotationConfig.getCategories().size() < 2) {
            throw new LiteFlowAIException("At least two categories are required for multi-label classification");
        }
    }

    @Override
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);
        return chatModel.chat(processorContext.getModelRequest().toChatRequest());
    }
}
