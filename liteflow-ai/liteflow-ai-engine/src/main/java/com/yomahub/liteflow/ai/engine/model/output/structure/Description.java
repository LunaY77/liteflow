package com.yomahub.liteflow.ai.engine.model.output.structure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述注解，用于为类或字段添加描述信息。
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    /**
     * 描述信息
     */
    String[] value();

    /**
     * 是否为必填项，默认为 true。
     */
    boolean required() default true;
}
