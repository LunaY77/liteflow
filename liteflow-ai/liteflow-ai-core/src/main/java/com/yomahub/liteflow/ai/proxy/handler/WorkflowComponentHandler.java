package com.yomahub.liteflow.ai.proxy.handler;

import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.annotation.Annotation;

/**
 * Workflow AI 组件处理器
 * <p>
 * 处理同时带有工作流注解和 @AIComponent 注解的组件
 *
 * @author 苍镜月
 * @since TODO
 */
public abstract class WorkflowComponentHandler<T extends Annotation> extends AbstractAIComponentHandler<T> {

    private static final String INTERCEPT_METHOD_NAME = "process";

    @Override
    public AITypeEnum getAIType() {
        return AITypeEnum.WORKFLOW;
    }

    @Override
    protected ElementMatcher<? super MethodDescription> getInterceptMethodName(AIProxyWrapBean<T> wrapBean) {
        return ElementMatchers.named(INTERCEPT_METHOD_NAME);
    }
}
