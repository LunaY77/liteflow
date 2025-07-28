package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.annotation.AIClassify;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * AI 意图识别注解处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyAnnotationProcessor extends AbstractAnnotationProcessor<AIClassify, ClassifyProxyWrapBean> {

    @Override
    public void postProcessBeforeTrigger(AIClassify annotation, ProcessorContext<ClassifyProxyWrapBean> context) {
        ClassifyProxyWrapBean wrapBean = context.getWrapBean();

        SetUtil.setIfPresent(wrapBean::setCategories, Arrays.stream(annotation.categories()).collect(Collectors.toList()));

        SetUtil.setIfPresent(wrapBean::setMultiLabel, annotation.multiLabel());

        // 处理系统提示词
        parsePrompt(annotation.systemPrompt(), context, wrapBean::setSystemPrompt);

        // 处理用户提示词
        parsePrompt(annotation.userPrompt(), context, wrapBean::setUserPrompt);
    }

    @Override
    public void postProcessAfterTrigger(AIClassify annotation, ProcessorContext<ClassifyProxyWrapBean> context) {

    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.CLASSIFY;
    }
}
