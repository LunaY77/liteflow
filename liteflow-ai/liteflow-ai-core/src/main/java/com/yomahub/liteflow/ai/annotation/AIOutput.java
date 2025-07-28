package com.yomahub.liteflow.ai.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 输出节点注解
 *
 * @author 苍镜月
 * @since TODO
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIOutput {

    /**
     * 输出字段映射配置
     *
     * @return 输出字段数组
     */
    OutputField[] mapping() default {};
}
