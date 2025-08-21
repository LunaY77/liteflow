package com.yomahub.liteflow.ai.proxy;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Objects;

/**
 * AI组件后置处理器
 * 动态代理，从 AI 组件持有类中获取 AI 组件接口并动态代理为具体的 AI 组件实现类，最终注册到 Spring 容器和 LiteFlow 容器中。
 *
 * @author 苍镜月
 * @since TODO
 */
public class AIComponentBeanPostProcessor implements BeanPostProcessor {

    private static final LFLog LOG = LFLoggerManager.getLogger(AIComponentBeanPostProcessor.class);

    private final AIComponentFactory componentFactory = AIComponentFactory.getInstance();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();

        // bean的类信息
        LOG.info("Processing bean: {}, class: {}, interfaces: {}",
                beanName, clazz.getName(), clazz.getInterfaces());

        // 检查是否是AI组件接口
        if (componentFactory.isAIComponent(clazz)) {
            LOG.info("Detected AI component interface: {} with beanName: {}", clazz.getName(), beanName);

            try {
                // 获取原始接口类
                Class<?> interfaceClass = ((ProxyInterfaceAware<?>) bean).getProxiedInterface();
                // 检查接口类是否被AI组件注解标记
                if (!componentFactory.isAIAnnotated(interfaceClass)) {
                    throw new LiteFlowAIException("AI component interface must be annotated with @AIComponent and exactly one type of AI annotation, beanName: " + beanName);
                }
                // 使用工厂创建AI组件
                NodeComponent aiComponent = componentFactory.createAIComponent(interfaceClass, beanName);

                if (Objects.isNull(aiComponent)) return bean;

                LOG.info("AI proxy component[{}] has been created for interface: {}", beanName, interfaceClass.getName());

                String nodeId = StrUtil.isNotBlank(aiComponent.getNodeId()) ? aiComponent.getNodeId() : beanName;
                // 注册到FlowBus中（LiteFlow容器）
                FlowBus.addManagedNode(nodeId, aiComponent);

                return aiComponent;
            } catch (Exception e) {
                LOG.error("Error creating AI component, beanName: {}", beanName, e);
                // 如果创建失败，返回原始bean而不是抛出异常
                return bean;
            }
        }

        return bean;
    }
}
