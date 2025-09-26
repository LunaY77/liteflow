package com.yomahub.liteflow.ai.engine.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在方法上使用该注解，将被识别为可供 LLM 调用的工具/函数。
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Tool {

    /**
     * Tool 名称。如果未提供，将使用方法名。
     */
    String name() default "";

    /**
     * 工具的描述。
     * 应该清晰且具有描述性，以便语言模型理解工具的目的和预期用途。
     */
    String[] value() default {};
}
