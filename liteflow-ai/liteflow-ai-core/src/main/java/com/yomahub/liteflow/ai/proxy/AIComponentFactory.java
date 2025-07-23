package com.yomahub.liteflow.ai.proxy;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.handler.AbstractAIComponentHandler;
import com.yomahub.liteflow.ai.proxy.handler.ChatComponentHandler;
import com.yomahub.liteflow.ai.proxy.handler.ClassifyComponentHandler;
import com.yomahub.liteflow.ai.proxy.handler.RetrievalComponentHandler;
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

    public AIComponentFactory() {
        this.handlerMap = new HashMap<>();
        initializeHandlers();
    }

    /**
     * 初始化处理器映射
     */
    private void initializeHandlers() {
        // 注册不同类型的AI组件处理器
        registerHandler(new ChatComponentHandler());
        registerHandler(new ClassifyComponentHandler());
        registerHandler(new RetrievalComponentHandler());

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
     * 判断是否是AI组件接口
     *
     * @param interfaceClass 接口类
     * @return true如果是AI组件
     */
    public boolean isAIComponent(Class<?> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            return false;
        }

        // 检查是否有AIComponent注解
        if (!interfaceClass.isAnnotationPresent(AIComponent.class)) {
            return false;
        }

        // 检查是否有任何支持的AI注解
        for (Class<? extends Annotation> annotationType : handlerMap.keySet()) {
            if (interfaceClass.isAnnotationPresent(annotationType)) {
                return true;
            }
        }

        return false;
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
