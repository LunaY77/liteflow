package com.yomahub.liteflow.ai.proxy.handler;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.core.NodeComponent;

import java.lang.annotation.Annotation;

/**
 * AI组件处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface AIComponentHandler<T extends Annotation> {

    /**
     * 创建AI组件
     *
     * @param interfaceClass 接口类
     * @param beanName       bean名称
     * @param aiComponent    AI组件注解
     * @return NodeComponent实例
     */
    NodeComponent createAIComponent(Class<?> interfaceClass, String beanName, AIComponent aiComponent);

    /**
     * 获取支持的注解类型
     *
     * @return 注解Class
     */
    Class<T> getSupportedAnnotationType();

    /**
     * 判断是否支持指定的注解类型
     *
     * @param annotationType 注解类型
     * @return true如果支持
     */
    default boolean supports(Class<? extends Annotation> annotationType) {
        return getSupportedAnnotationType().equals(annotationType);
    }
}
