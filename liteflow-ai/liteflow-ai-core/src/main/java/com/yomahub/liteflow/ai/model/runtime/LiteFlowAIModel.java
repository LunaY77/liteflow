package com.yomahub.liteflow.ai.model.runtime;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记注解，标识 LiteFlow AI 的大模型运行时。
 * 被标记的类会自动注册到 {@link ModelRuntimeFactory} 中。
 *
 * @author 苍镜月
 * @since TODO
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public @interface LiteFlowAIModel {

    /**
     * 模型名称
     *
     * @return 模型名称
     */
    String value();
}
