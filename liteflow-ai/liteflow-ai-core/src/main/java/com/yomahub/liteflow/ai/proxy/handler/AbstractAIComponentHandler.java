package com.yomahub.liteflow.ai.proxy.handler;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.exception.ProxyException;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import com.yomahub.liteflow.util.SerialsUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.util.Objects;

/**
 * AI组件处理器抽象基类
 *
 * @author 苍镜月
 * @since TODO
 */
public abstract class AbstractAIComponentHandler<T extends Annotation> {

    protected final LFLog LOG = LFLoggerManager.getLogger(this.getClass());

    /**
     * 获取对应的 AI 组件类型
     *
     * @return {@link AITypeEnum}
     */
    public abstract AITypeEnum getAIType();

    /**
     * 获取支持的注解类型
     *
     * @return 注解Class
     */
    public abstract Class<T> getSupportedAnnotationType();

    /**
     * 创建AI组件
     *
     * @param interfaceClass 接口类
     * @param beanName       bean名称
     * @param aiComponent    AI组件注解
     * @return NodeComponent实例
     */
    public NodeComponent createAIComponent(Class<?> interfaceClass, String beanName, AIComponent aiComponent) {
        LOG.info("Creating AI component for interface: {}, beanName: {}, type: {}",
                interfaceClass.getName(), beanName, getAIType());

        // 解析AI注解
        AIProxyWrapBean<T> wrapBean = parseAIAnnotations(interfaceClass, beanName, aiComponent);
        if (Objects.isNull(wrapBean)) {
            LOG.warn("Failed to parse AI annotations for interface: {}, beanName: {}",
                    interfaceClass.getName(), beanName);
            return null;
        }

        // 创建代理组件
        return createProxyComponent(wrapBean);
    }

    /**
     * 解析AI注解信息
     *
     * @param interfaceClass 接口类
     * @param beanName       bean名称
     * @param aiComponent    AI组件注解
     * @return 包装bean
     */
    public AIProxyWrapBean<T> parseAIAnnotations(Class<?> interfaceClass, String beanName, AIComponent aiComponent) {
        LOG.info("Parsing AI annotations for interface: {}, beanName: {}", interfaceClass.getName(), beanName);

        T aiAnnotation = findAIAnnotation(interfaceClass);
        // 如果没有找到 AI 注解，则记录警告并返回 null
        if (Objects.isNull(aiAnnotation)) {
            LOG.warn("No {} annotation found for interface: {}, beanName: {}",
                    getSupportedAnnotationType().getSimpleName(), interfaceClass.getName(), beanName);
            return null;
        }

        return createWrapBean(aiComponent, aiAnnotation, interfaceClass, beanName);
    }

    /**
     * 查找AI注解
     *
     * @param clazz 类
     * @return AI注解
     */
    protected T findAIAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(getSupportedAnnotationType());
    }

    /**
     * 创建包装Bean
     *
     * @param aiComponent    AI组件注解
     * @param annotation     具体AI注解
     * @param interfaceClass 接口类
     * @param beanName       bean名称
     * @return 包装bean
     */
    protected abstract AIProxyWrapBean<T> createWrapBean(AIComponent aiComponent, T annotation,
                                                         Class<?> interfaceClass, String beanName);

    /**
     * 创建代理组件
     *
     * @param wrapBean 包装bean
     * @return NodeComponent实例
     */
    private NodeComponent createProxyComponent(AIProxyWrapBean<T> wrapBean) {
        AITypeEnum aiType = getAIType();

        Class<? extends NodeComponent> nodeComponentClass = aiType.getComponentClass();

        // 创建 ByteBuddy 代理
        try {
            NodeComponent nodeComponent = new ByteBuddy()
                    .subclass(nodeComponentClass)
                    .name(generateProxyClassName(wrapBean))
                    .implement(wrapBean.getInterfaceClass())
                    .method(getInterceptMethodName(wrapBean))
                    .intercept(InvocationHandlerAdapter.of(getInvocationHandler(wrapBean)))
                    .make()
                    .load(this.getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded()
                    .newInstance();

            SetUtil.setIfPresent(nodeComponent::setNodeId, wrapBean.getNodeId());
            SetUtil.setIfPresent(nodeComponent::setName, wrapBean.getNodeName());

            LOG.info("Created AI component: {}, beanName: {}, type: {}",
                    nodeComponent.getNodeId(), wrapBean.getBeanName(), aiType);

            return nodeComponent;
        } catch (Exception e) {
            throw new ProxyException(e);
        }
    }

    /**
     * 生成代理类名称
     *
     * @param wrapBean 包装bean
     * @return 代理类名称
     */
    private String generateProxyClassName(AIProxyWrapBean<T> wrapBean) {
        return StrUtil.format("{}$ByteBuddy${}${}",
                wrapBean.getInterfaceClass().getName(),
                wrapBean.getNodeId(),
                SerialsUtil.generateShortUUID());
    }

    /**
     * 获取InvocationHandler
     *
     * @param wrapBean 包装bean
     * @return InvocationHandler实例
     */
    protected abstract InvocationHandler getInvocationHandler(AIProxyWrapBean<T> wrapBean);

    /**
     * 获取拦截方法名称
     *
     * @return 拦截方法名称
     */
    protected abstract ElementMatcher<? super MethodDescription> getInterceptMethodName(AIProxyWrapBean<T> wrapBean);

    /**
     * 判断是否支持指定的注解类型
     *
     * @param annotationType 注解类型
     * @return true如果支持
     */
    public boolean supports(Class<? extends Annotation> annotationType) {
        return getSupportedAnnotationType().equals(annotationType);
    }
}
