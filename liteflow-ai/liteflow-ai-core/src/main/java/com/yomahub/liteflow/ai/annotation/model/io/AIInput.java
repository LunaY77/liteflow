package com.yomahub.liteflow.ai.annotation.model.io;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 输入节点注解
 *
 * @author 苍镜月
 * @since 2.16.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIInput {

    /**
     * 输入字段映射配置
     *
     * @return 输入字段数组
     */
    InputField[] mapping() default {};
}
