package com.yomahub.liteflow.ai.config;

import com.yomahub.liteflow.springboot.config.LiteflowMainAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Configuration
@AutoConfigureBefore({ LiteflowMainAutoConfiguration.class })
public class LiteflowAIAutoConfiguration {

}
