package com.yomahub.liteflow.ai.workflow.coze.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Coze 工作流自动配置类
 *
 * @author 苍镜月
 * @since TODO
 */

@Configuration
@EnableConfigurationProperties(CozeWorkflowProperty.class)
public class CozeWorkflowAutoConfiguration {

}
