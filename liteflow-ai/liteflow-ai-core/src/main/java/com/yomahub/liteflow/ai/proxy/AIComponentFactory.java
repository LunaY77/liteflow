package com.yomahub.liteflow.ai.proxy;

import cn.hutool.core.util.ServiceLoaderUtil;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.handler.AIComponentHandler;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AI组件工厂
 * 实现工厂模式，管理不同类型AI组件的创建
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class AIComponentFactory {

    private static final LFLog LOG = LFLoggerManager.getLogger(AIComponentFactory.class);

    private static final Map<Class<? extends Annotation>, AIComponentHandler<?>> HANDLER_REGISTRY;

    static {
        // 使用SPI机制注册所有AI组件处理器
        HANDLER_REGISTRY = ServiceLoaderUtil.loadList(AIComponentHandler.class)
                .stream()
                .peek(h -> LOG.info("Discovered AI component handler: {} for annotation: {}",
                        h.getClass().getName(),
                        h.getSupportedAnnotationType().getSimpleName()))
                .collect(Collectors.toConcurrentMap(
                        AIComponentHandler::getSupportedAnnotationType,
                        h -> h));
    }

    private AIComponentFactory() {
        // 私有构造函数，使用静态方法访问
    }

    /**
     * 创建AI组件
     *
     * @param interfaceClass 接口类
     * @param beanName       bean名称
     * @return NodeComponent实例，如果不是AI组件则返回null
     */
    public static NodeComponent createAIComponent(Class<?> interfaceClass, String beanName) {
        // 检查是否是AI组件
        AIComponent aiComponent = interfaceClass.getAnnotation(AIComponent.class);
        if (Objects.isNull(aiComponent)) {
            LOG.debug("Interface {} is not an AI component", interfaceClass.getName());
            return null;
        }

        // 查找对应的处理器
        AIComponentHandler<?> handler = findHandler(interfaceClass);
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
    private static AIComponentHandler<?> findHandler(Class<?> interfaceClass) {
        // 遍历所有支持的注解类型，查找匹配的处理器
        for (Map.Entry<Class<? extends Annotation>, AIComponentHandler<?>> entry : HANDLER_REGISTRY.entrySet()) {
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
    public static boolean isAIComponent(Class<?> clazz) {
        // 判断是否实现了唯一标识 ProxyInterfaceAware 接口
        return ProxyInterfaceAware.class.isAssignableFrom(clazz);
    }

    /**
     * 检查类是否被AI注解标记
     *
     * @param clazz 类
     * @return 如果类被AIComponent注解标记，并且至少有一个支持的AI注解
     */
    public static boolean isAIAnnotated(Class<?> clazz) {
        // 检查是否有AIComponent注解
        if (!clazz.isAnnotationPresent(AIComponent.class)) {
            return false;
        }

        // 检查是否有支持的AI注解
        return HANDLER_REGISTRY.keySet().stream()
                .anyMatch(clazz::isAnnotationPresent);
    }

    /**
     * 获取支持的注解类型
     *
     * @return 支持的注解类型集合
     */
    public static Map<Class<? extends Annotation>, AIComponentHandler<?>> getSupportedAnnotations() {
        return new HashMap<>(HANDLER_REGISTRY);
    }
}
