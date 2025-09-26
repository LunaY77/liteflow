package com.yomahub.liteflow.ai.proxy.handler;

import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.proxy.invocation.ChatAIInvocationHandler;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;

/**
 * 聊天组件处理器
 * 具体策略实现
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class ChatComponentHandler extends AbstractAIComponentHandler<AIChat> {

    private static final String INTERCEPT_METHOD_NAME = "process";

    @Override
    public AITypeEnum getAIType() {
        return AITypeEnum.CHAT;
    }

    @Override
    public Class<AIChat> getSupportedAnnotationType() {
        return AIChat.class;
    }

    @Override
    protected AIProxyWrapBean<AIChat> createWrapBean(AIComponent aiComponent, AIChat annotation,
                                                     Class<?> interfaceClass, String beanName) {
        return new ChatProxyWrapBean(aiComponent, annotation, interfaceClass, beanName);
    }

    @Override
    protected InvocationHandler getInvocationHandler(AIProxyWrapBean<AIChat> wrapBean) {
        return new ChatAIInvocationHandler((ChatProxyWrapBean) wrapBean);
    }

    @Override
    protected ElementMatcher<? super MethodDescription> getInterceptMethodName(AIProxyWrapBean<AIChat> wrapBean) {
        return ElementMatchers.named(INTERCEPT_METHOD_NAME);
    }
}