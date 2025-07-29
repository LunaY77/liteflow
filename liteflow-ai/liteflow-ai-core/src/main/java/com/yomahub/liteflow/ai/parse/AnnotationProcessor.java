package com.yomahub.liteflow.ai.parse;

import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;

import java.lang.annotation.Annotation;

/**
 * 粉色奶龙处理器接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface AnnotationProcessor<A extends Annotation, T extends AIProxyWrapBean<A>> {

    /**
     * 执行注解解析前处理
     *
     * @param annotation 待解析注解
     * @param context    处理器上下文
     */
    void postProcessBeforeTrigger(A annotation, ProcessorContext<T> context);

    /**
     * 执行注解解析后处理
     *
     * @param annotation 待解析注解
     * @param context    处理器上下文
     * @param result     响应结果
     */
    void postProcessAfterTrigger(A annotation, ProcessorContext<T> context, Object result);
}
