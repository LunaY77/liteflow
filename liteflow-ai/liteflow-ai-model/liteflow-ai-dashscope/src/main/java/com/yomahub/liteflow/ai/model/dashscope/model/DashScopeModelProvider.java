package com.yomahub.liteflow.ai.model.dashscope.model;

import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.ModelProviderRegistrar;
import com.yomahub.liteflow.ai.model.dashscope.constants.DashScopeConstant;
import com.yomahub.liteflow.ai.model.dashscope.model.chat.DashScopeChatModel;
import com.yomahub.liteflow.ai.model.dashscope.model.chat.DashScopeChatRequest;

import java.util.Optional;

import static com.yomahub.liteflow.ai.util.SetUtil.setIfPresent;

/**
 * DashScope 模型提供者注册类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class DashScopeModelProvider extends ModelProviderRegistrar {

    @Override
    public Optional<ChatRequest.Builder<?>> createChatRequestBuilder() {
        return Optional.of(new DashScopeChatRequest.Builder());
    }

    @Override
    public Optional<ChatModel> createChatModel(ModelConfigAggregator configAggregator) {
        return Optional.of(DashScopeChatModel.builder())
                // 模型 Config 配置
                .map(builder -> {
                    setIfPresent(builder::apiUrl, configAggregator.getApiUrl(), DashScopeConstant.DEFAULT_API_URL);
                    setIfPresent(builder::endPoint, configAggregator.getEndPoint(), DashScopeConstant.DEFAULT_END_POINT);
                    setIfPresent(builder::apiKey, configAggregator.getApiKey());
                    setIfPresent(builder::provider, configAggregator.getProvider(), DashScopeConstant.PROVIDER_NAME);
                    setIfPresent(builder::model, configAggregator.getModel());
                    setIfPresent(builder::connectTimeout, configAggregator.getConnectTimeout());
                    setIfPresent(builder::readTimeout, configAggregator.getReadTimeout());
                    setIfPresent(builder::headersConfig, configAggregator.getCustomHeaders());
                    setIfPresent(builder::autoToolCallEnabled, configAggregator.getAutoToolCallEnabled().toBool());
                    return builder;
                })
                // 构建模型
                .map(DashScopeChatModel.Builder::build);
    }

    @Override
    public String getProviderName() {
        return DashScopeConstant.PROVIDER_NAME;
    }
}
