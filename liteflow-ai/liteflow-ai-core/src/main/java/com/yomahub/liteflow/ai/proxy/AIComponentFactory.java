package com.yomahub.liteflow.ai.proxy;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.handler.AbstractAIComponentHandler;
import com.yomahub.liteflow.ai.proxy.handler.ChatComponentHandler;
import com.yomahub.liteflow.ai.proxy.handler.ClassifyComponentHandler;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * AI组件工厂
 * 实现工厂模式，管理不同类型AI组件的创建
 *
 * @author 苍镜月
 * @since TODO
 */
public class AIComponentFactory {

    private static final LFLog LOG = LFLoggerManager.getLogger(AIComponentFactory.class);

    private final Map<Class<? extends Annotation>, AbstractAIComponentHandler<?>> handlerMap;

    private AIComponentFactory() {
        this.handlerMap = new HashMap<>();
        initializeHandlers();
    }

    private static class Holder {
        private static final AIComponentFactory INSTANCE = new AIComponentFactory();
    }

    /**
     * 获取AIComponentFactory单例实例
     *
     * @return AIComponentFactory实例
     */
    public static AIComponentFactory getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 初始化处理器映射
     */
    private void initializeHandlers() {
        // 注册不同类型的AI组件处理器
        registerHandler(new ChatComponentHandler());
        registerHandler(new ClassifyComponentHandler());

        LOG.info("Initialized AI component handlers: {}", handlerMap.keySet());
    }

    /**
     * 注册处理器
     *
     * @param handler 处理器实例
     */
    private void registerHandler(AbstractAIComponentHandler<?> handler) {
        handlerMap.put(handler.getSupportedAnnotationType(), handler);
        LOG.debug("Registered handler: {} for annotation: {}",
                handler.getClass().getSimpleName(),
                handler.getSupportedAnnotationType().getSimpleName());
    }

    /**
     * 创建AI组件
     *
     * @param interfaceClass 接口类
     * @param beanName       bean名称
     * @return NodeComponent实例，如果不是AI组件则返回null
     */
    public NodeComponent createAIComponent(Class<?> interfaceClass, String beanName) {
        // 检查是否是AI组件
        AIComponent aiComponent = interfaceClass.getAnnotation(AIComponent.class);
        if (Objects.isNull(aiComponent)) {
            LOG.debug("Interface {} is not an AI component", interfaceClass.getName());
            return null;
        }

        // 查找对应的处理器
        AbstractAIComponentHandler<?> handler = findHandler(interfaceClass);
        if (Objects.isNull(handler)) {
            LOG.warn("No handler found for AI component interface: {}", interfaceClass.getName());
            return null;
        }

        // 使用处理器创建组件
        try {
            NodeComponent component = handler.createAIComponent(interfaceClass, beanName, aiComponent);
            if (Objects.nonNull(component)) {
                LOG.info("Successfully created AI component: {} with handler: {}",
                        beanName, handler.getClass().getSimpleName());
            }
            return component;
        } catch (Exception e) {
            LOG.error("Failed to create AI component: {}", beanName, e);
            throw new RuntimeException("Failed to create AI component: " + beanName, e);
        }
    }

    /**
     * 查找适合的处理器
     *
     * @param interfaceClass 接口类
     * @return 处理器实例，如果没有找到则返回null
     */
    private AbstractAIComponentHandler<?> findHandler(Class<?> interfaceClass) {
        // 遍历所有支持的注解类型，查找匹配的处理器
        for (Map.Entry<Class<? extends Annotation>, AbstractAIComponentHandler<?>> entry : handlerMap.entrySet()) {
            Class<? extends Annotation> annotationType = entry.getKey();
            if (interfaceClass.isAnnotationPresent(annotationType)) {
                LOG.debug("Found handler: {} for annotation: {} on interface: {}",
                        entry.getValue().getClass().getSimpleName(),
                        annotationType.getSimpleName(),
                        interfaceClass.getName());
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 判断是否是AI组件，通过检查是否实现了唯一标识的接口
     *
     * @param clazz 类
     * @return 如果是AI组件
     */
    public boolean isAIComponent(Class<?> clazz) {
        // 判断是否实现了唯一标识 ProxyMetadataAware 接口
        return ProxyInterfaceAware.class.isAssignableFrom(clazz);
    }

    /**
     * 检查类是否被AI注解标记
     *
     * @param clazz 类
     * @return 如果类被AIComponent注解标记，并且至少有一个支持的AI注解
     */
    public boolean isAIAnnotated(Class<?> clazz) {
        // 检查是否有AIComponent注解
        if (!clazz.isAnnotationPresent(AIComponent.class)) {
            return false;
        }

        // 检查是否只有一个支持的AI注解
        return handlerMap.keySet().stream()
                .filter(clazz::isAnnotationPresent)
                .count() == 1;
    }

    /**
     * 获取支持的注解类型
     *
     * @return 支持的注解类型集合
     */
    public Map<Class<? extends Annotation>, AbstractAIComponentHandler<?>> getSupportedAnnotations() {
        return new HashMap<>(handlerMap);
    }
}
