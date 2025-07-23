package com.yomahub.liteflow.ai.proxy.handler;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.AIRetrieval;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.proxy.invocation.RetrievalAIInvocationHandler;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;

/**
 * 检索组件处理器
 * 具体策略实现
 *
 * @author 苍镜月
 * @since TODO
 */
public class RetrievalComponentHandler extends AbstractAIComponentHandler<AIRetrieval> {

    private static final String INTERCEPT_METHOD_NAME = "process";

    @Override
    public AITypeEnum getAIType() {
        return AITypeEnum.RETRIEVAL;
    }

    @Override
    public Class<AIRetrieval> getSupportedAnnotationType() {
        return AIRetrieval.class;
    }

    @Override
    protected AIProxyWrapBean<AIRetrieval> createWrapBean(AIComponent aiComponent, AIRetrieval annotation,
                                                          Class<?> interfaceClass, String beanName) {
        return new AIProxyWrapBean<>(aiComponent, annotation, interfaceClass, beanName);
    }

    @Override
    protected InvocationHandler getInvocationHandler(AIProxyWrapBean<AIRetrieval> wrapBean) {
        return new RetrievalAIInvocationHandler(wrapBean);
    }

    @Override
    protected ElementMatcher<? super MethodDescription> getInterceptMethodName() {
        return ElementMatchers.named(INTERCEPT_METHOD_NAME);
    }
}