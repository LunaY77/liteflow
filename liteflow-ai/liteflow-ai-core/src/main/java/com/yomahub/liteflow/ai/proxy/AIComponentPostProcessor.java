package com.yomahub.liteflow.ai.proxy;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import com.yomahub.liteflow.process.holder.SpringNodeIdHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Objects;

/**
 * AI组件后置处理器
 *
 * @author 苍镜月
 * @since TODO
 */
public class AIComponentPostProcessor implements BeanPostProcessor {

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

                if (Objects.isNull(aiComponent)) return bean;

                LOG.info("AI proxy component[{}] has been created for interface: {}", beanName, clazz.getName());

                String nodeId = StrUtil.isNotBlank(aiComponent.getNodeId()) ? aiComponent.getNodeId() : SpringNodeIdHolder.getRealBeanName(clazz, beanName);
                SpringNodeIdHolder.add(nodeId);

                return aiComponent;
            } catch (Exception e) {
                LOG.error("Error creating AI component for interface: {}, beanName: {}",
                        clazz.getName(), beanName, e);
                // 如果创建失败，返回原始bean而不是抛出异常
                return bean;
            }
        }

        return bean;
    }
}
