package com.yomahub.liteflow.test.ai.mock.mockbean;

import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.ModelProvider;

import java.util.Objects;
import java.util.Optional;

/**
 * 动态 Mock Provider
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ConfigurableMockModelProvider implements ModelProvider {

    @Override
    public Optional<ChatModel> createChatModel(ModelConfigAggregator configAggregator) {
        // mock config, 占位用，仅填写必须字段
        ChatConfig config = (ChatConfig) ChatConfig.builder()
                .apiUrl("mock-api-url")
                .endPoint("mock-end-point")
                // 沿用原来的 Provider
                .provider(configAggregator.getProvider())
                .model("mock-model")
                .build();
        return Optional.of(new MockChatModel(config));
    }

    @Override
    public String getProviderName() {
        MockConfig config = MockConfigHolder.getMockConfig();
        if (Objects.nonNull(config)) {
            // 动态返回当前线程配置的 Provider 名称
            return config.getProvider().getProviderName();
        }
        // 返回一个默认值，或者抛出异常
        return "other";
    }
}
