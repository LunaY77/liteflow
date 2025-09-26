package com.yomahub.liteflow.ai.parse;

import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;

import java.lang.annotation.Annotation;

/**
 * 粉色奶龙处理器接口
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface AnnotationProcessor<A extends Annotation, C extends ParsedAnnotationConfig> {

    /**
     * 执行注解解析前处理
     *
     * @param annotation 待解析注解
     * @param context    处理器上下文
     */
    void postProcessBeforeTrigger(A annotation, ProcessorContext<C> context);

    /**
     * 执行注解解析后处理
     *
     * @param context    处理器上下文
     * @param result     响应结果
     */
    void postProcessAfterTrigger(ProcessorContext<C> context, Object result);
}
