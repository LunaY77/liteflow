package com.yomahub.liteflow.ai.proxy;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.proxy.annotation.EnableAIComponent;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import com.yomahub.liteflow.process.holder.SpringNodeIdHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class AIComponentProxyRegistrar implements ImportBeanDefinitionRegistrar {

    private static final LFLog LOG = LFLoggerManager.getLogger(AIComponentProxyRegistrar.class);

    private final AIComponentFactory aiComponentFactory = new AIComponentFactory();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 @EnableAIComponent 注解的属性
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableAIComponent.class.getName());
        if (Objects.isNull(annotationAttributes) || annotationAttributes.isEmpty()) {
            return;
        }

        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        if (basePackages.length == 0) {
            return;
        }

        // 创建类路径扫描器
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
            }
        };

        // 添加过滤器，仅寻找带有 @AIComponent 注解的接口
        scanner.addIncludeFilter(new AnnotationTypeFilter(AIComponent.class));

        // 扫描指定包路径下的所有符合条件的接口
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidateComponents) {
                try {
                    // 获取接口的完整类名
                    String interfaceClassName = candidate.getBeanClassName();
                    Class<?> clazz = Class.forName(interfaceClassName);

                    String beanName = StringUtils.uncapitalize(clazz.getSimpleName());

                    if (aiComponentFactory.isAIComponent(clazz)) {
                        LOG.info("Detected AI component interface: {} with beanName: {}", interfaceClassName, beanName);
                        Object beanInstance = buildAIComponent(clazz, beanName);

                        if (Objects.nonNull(beanInstance) && registry instanceof SingletonBeanRegistry) {
                            ((SingletonBeanRegistry) registry).registerSingleton(beanName, beanInstance);
                            LOG.info("AI proxy component [{}] has been registered in the application context.", beanName);
                        } else {
                            LOG.warn("Failed to register AI proxy component [{}] in the application context.", beanName);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new LiteFlowAIException("Failed to find AI component interface class", e);
                }
            }
        }
    }

    private Object buildAIComponent(Class<?> clazz, String beanName) {
        try {
            // 使用工厂创建AI组件
            NodeComponent aiComponent = aiComponentFactory.createAIComponent(clazz, beanName);

            if (Objects.isNull(aiComponent)) return null;

            LOG.info("AI proxy component[{}] has been created for interface: {}", beanName, clazz.getName());

            String nodeId = StrUtil.isNotBlank(aiComponent.getNodeId()) ? aiComponent.getNodeId() : SpringNodeIdHolder.getRealBeanName(clazz, beanName);
            SpringNodeIdHolder.add(nodeId);

            FlowBus.addNode(nodeId, aiComponent.getName(), NodeTypeEnum.COMMON, aiComponent.getClass());

            return aiComponent;
        } catch (Exception e) {
            LOG.error("Error creating AI component for interface: {}, beanName: {}",
                    clazz.getName(), beanName, e);
            // 如果创建失败，返回原始bean而不是抛出异常
            return null;
        }
    }
}
