package com.yomahub.liteflow.ai.model.dashscope.config;

import com.yomahub.liteflow.ai.model.dashscope.model.DashScopeModelProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DashScope 自动配置类
 *
 * @author 苍镜月
 * @since TODO
 */

@Configuration
@EnableConfigurationProperties(DashScopeModelProperty.class)
public class DashScopeAutoConfiguration {

    @Bean
    public DashScopeModelProvider dashScopeModelProvider() {
        return new DashScopeModelProvider();
    }
}
