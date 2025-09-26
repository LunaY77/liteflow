package com.yomahub.liteflow.ai.workflow.dashscope.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里百炼工作流自动配置类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Configuration
@EnableConfigurationProperties(DashScopeWorkflowProperty.class)
public class DashScopeWorkflowAutoConfiguration {

}
