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
 * TODO
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

        SetUtil.setIfPresent(wrapBean::setSystemPrompt, annotation.systemPrompt());

        SetUtil.setIfPresent(wrapBean::setUserPrompt, annotation.userPrompt());
    }

    @Override
    public void postProcessAfterTrigger(AIClassify annotation, ProcessorContext<ClassifyProxyWrapBean> context) {

    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.CLASSIFY;
    }
}
