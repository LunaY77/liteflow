package com.yomahub.liteflow.ai.engine.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 工具方法参数注解。
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface ToolParam {

    /**
     * 参数名称。
     */
    String value();

    /**
     * 参数描述。
     * 应该清晰且具有描述性，以便语言模型理解参数的目的和预期用途。
     */
    boolean required() default true;
}
