package com.yomahub.liteflow.ai.proxy.annotation;

import com.yomahub.liteflow.ai.proxy.AIComponentProxyRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用AI组件的代理扫描注解，指定需要扫描接口的包路径
 *
 * @author 苍镜月
 * @since TODO
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(AIComponentProxyRegistrar.class) // 重要！！！动态发现接口并进行第一次动态代理的重要节点
public @interface EnableAIComponent {

    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * 指定要扫描的包路径
     *
     * @return 包路径数组
     */
    @AliasFor("value")
    String[] basePackages() default {};
}
