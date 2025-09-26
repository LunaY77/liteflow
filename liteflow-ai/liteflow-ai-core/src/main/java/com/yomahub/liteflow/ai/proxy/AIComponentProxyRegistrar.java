package com.yomahub.liteflow.ai.proxy;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * AI 组件发现器
 * 将接口存入 {@link AIComponentHolder} 注册为 {@link BeanDefinition} 到 Spring 容器中。
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class AIComponentProxyRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final LFLog LOG = LFLoggerManager.getLogger(AIComponentProxyRegistrar.class);

    private List<String> basePackages;

    private Boolean enable;

    /**
     * 通过 EnvironmentAware 获取配置信息
     *
     * @param environment Spring 环境对象
     */
    @Override
    public void setEnvironment(Environment environment) {
        Binder binder = Binder.get(environment);
        // 读取配置，LiteFlow-AI 是否开启，如果未设置代表默认值 True
        enable = binder.bind("liteflow.ai.enable", Boolean.class)
                .orElse(Boolean.TRUE);
        // 读取配置，LiteFlow-AI 基础包路径，如果未设置代表默认值空列表
        basePackages = binder.bind("liteflow.ai.base-packages", Bindable.listOf(String.class))
                .orElse(Collections.emptyList());
    }

    /**
     * 扫描指定包路径下的所有符合条件的接口，并将其注册为 BeanDefinition
     *
     * @param registry BeanDefinitionRegistry
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 检查是否启用 AI 组件，如果未启用则不进行任何操作
        if (Boolean.FALSE.equals(enable)) return;

        // 检查是否有包路径，如果没有则不进行任何操作
        if (basePackages.isEmpty()) return;

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
                    Class<?> interfaceClass = Class.forName(interfaceClassName);

                    // 获取 BeanDefinitionBuilder
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(AIComponentHolder.class);

                    // 注入 接口 Class
                    builder.addConstructorArgValue(interfaceClass);

                    // 设置依赖自动注入
                    builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

                    // 将 BeanDefinition 注册到 Spring 容器中
                    // NodeId 与 BeanName 保持一致
                    AIComponent anno = interfaceClass.getAnnotation(AIComponent.class);
                    String beanName = StrUtil.isNotBlank(anno.nodeId()) ? anno.nodeId() : StringUtils.uncapitalize(interfaceClass.getSimpleName());
                    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

                    LOG.info("Detected AI component interface: {}, registered as BeanDefinition with name: {}",
                            interfaceClassName, beanName);
                } catch (ClassNotFoundException e) {
                    throw new LiteFlowAIException("Failed to find AI component interface class", e);
                } catch (BeanDefinitionOverrideException e) {
                    throw new LiteFlowAIException("bean name conflicts with existing bean definition", e);
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 空实现
    }
}
