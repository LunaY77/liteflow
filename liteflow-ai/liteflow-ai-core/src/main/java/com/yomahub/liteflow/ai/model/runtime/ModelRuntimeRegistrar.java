package com.yomahub.liteflow.ai.model.runtime;

import com.yomahub.liteflow.ai.model.BaseModel;
import com.yomahub.liteflow.ai.model.ModelConfig;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.Objects;

/**
 * 运行时自动注册器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ModelRuntimeRegistrar implements SmartInitializingSingleton {

    private final LFLog LOG = LFLoggerManager.getLogger(ModelRuntimeRegistrar.class);

    @Override
    public void afterSingletonsInstantiated() {
        String[] beanNames = SpringUtil.getBeanNamesForAnnotation(LiteFlowAIModel.class);

        for (String beanName : beanNames) {
            Class<?> beanType = SpringUtil.getType(beanName);

            if (Objects.nonNull(beanType) && BaseModel.class.isAssignableFrom(beanType)) {
                LiteFlowAIModel annotation = beanType.getAnnotation(LiteFlowAIModel.class);
                String provider = annotation.value() + "_" + beanName;

                ModelRuntimeFactory.registerModelProvider(provider, (Class<? extends BaseModel<? extends ModelConfig>>) beanType);

                LOG.info("已注册模型运行时: {} -> {}", provider, beanType.getName());
            }
        }
    }
}
