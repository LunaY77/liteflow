package com.yomahub.liteflow.ai.proxy.handler;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.proxy.invocation.ClassifyAIInvocationHandler;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;

/**
 * 分类组件处理器
 * 具体策略实现
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class ClassifyComponentHandler extends AbstractAIComponentHandler<AIClassify> {

    private static final String INTERCEPT_SWITCH_METHOD_NAME = "processSwitch";

    @Override
    public AITypeEnum getAIType() {
        return AITypeEnum.CLASSIFY;
    }

    @Override
    public Class<AIClassify> getSupportedAnnotationType() {
        return AIClassify.class;
    }

    @Override
    protected AIProxyWrapBean<AIClassify> createWrapBean(AIComponent aiComponent, AIClassify annotation,
                                                         Class<?> interfaceClass, String beanName) {
        return new ClassifyProxyWrapBean(aiComponent, annotation, interfaceClass, beanName);
    }

    @Override
    protected InvocationHandler getInvocationHandler(AIProxyWrapBean<AIClassify> wrapBean) {
        return new ClassifyAIInvocationHandler((ClassifyProxyWrapBean) wrapBean);
    }

    @Override
    protected ElementMatcher<? super MethodDescription> getInterceptMethodName(AIProxyWrapBean<AIClassify> wrapBean) {
        return ElementMatchers.named(INTERCEPT_SWITCH_METHOD_NAME);
    }
}