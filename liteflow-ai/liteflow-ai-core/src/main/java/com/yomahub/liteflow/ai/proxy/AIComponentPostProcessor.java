package com.yomahub.liteflow.ai.proxy;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import java.util.Objects;

/**
 * AI组件后置处理器
 *
 * @author 苍镜月
 * @since TODO
 */
public class AIComponentPostProcessor implements BeanPostProcessor, Ordered {

    private static final LFLog LOG = LFLoggerManager.getLogger(AIComponentPostProcessor.class);

    private final AIComponentFactory aiComponentFactory = new AIComponentFactory();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();

        // 检查是否是AI组件接口
        if (aiComponentFactory.isAIComponent(clazz)) {
            LOG.info("Detected AI component interface: {} with beanName: {}", clazz.getName(), beanName);

            try {
                // 使用工厂创建AI组件
                NodeComponent aiComponent = aiComponentFactory.createAIComponent(clazz, beanName);

                if (Objects.nonNull(aiComponent)) {
                    LOG.info("Successfully created AI component for interface: {}, replacing bean: {}",
                            clazz.getName(), beanName);
                    return aiComponent;
                } else {
                    LOG.warn("Failed to create AI component for interface: {}, returning original bean",
                            clazz.getName());
                    return bean;
                }

            } catch (Exception e) {
                LOG.error("Error creating AI component for interface: {}, beanName: {}",
                        clazz.getName(), beanName, e);
                // 如果创建失败，返回原始bean而不是抛出异常
                return bean;
            }
        }

        return bean;
    }

    @Override
    public int getOrder() {
        // 设置较高的优先级，确保在其他后置处理器之前执行
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
