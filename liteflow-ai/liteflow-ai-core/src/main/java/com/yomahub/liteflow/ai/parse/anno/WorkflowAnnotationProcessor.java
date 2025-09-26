package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;

import java.lang.annotation.Annotation;

/**
 * Workflow注解处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class WorkflowAnnotationProcessor extends AbstractAnnotationProcessor<Annotation, ParsedAnnotationConfig> {

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.WORKFLOW;
    }

    @Override
    public void postProcessBeforeTrigger(Annotation annotation, ProcessorContext<ParsedAnnotationConfig> context) {
    }

    @Override
    public void postProcessAfterTrigger(ProcessorContext<ParsedAnnotationConfig> context, Object result) {
        mapOutput2Context(context, result);
    }
}
