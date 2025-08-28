package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.domain.dto.ParsedClassifyAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;

import java.util.List;

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
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);
        ChatResponse response = chatModel.chat(processorContext.getModelRequest().toChatRequest());
        ParsedClassifyAnnotationConfig annotationConfig = (ParsedClassifyAnnotationConfig) processorContext.getParsedAnnotationConfig();
        // 如果是多标签则返回结构化转换的list, 单标签返回 String
        if (annotationConfig.isMultiLabel()) {
            List<String> resList = (List<String>) response.as(processorContext.getModelRequest().toChatRequest().getOutputParser());
            return String.join(",", resList);
        } else {
            return response.getContent().getContent();
        }
    }
}
