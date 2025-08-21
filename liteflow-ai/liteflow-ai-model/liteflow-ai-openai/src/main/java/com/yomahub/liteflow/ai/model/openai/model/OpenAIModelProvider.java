package com.yomahub.liteflow.ai.model.openai.model;

import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.ModelProviderRegistrar;
import com.yomahub.liteflow.ai.model.openai.constants.OpenAIConstant;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatModel;
import com.yomahub.liteflow.ai.model.openai.model.chat.OpenAIChatRequest;

import java.util.Optional;

import static com.yomahub.liteflow.ai.util.SetUtil.setIfPresent;

/**
 * OpenAI 模型提供者。
 *
 * @author 苍镜月
 * @since TODO
 */

public class OpenAIModelProvider extends ModelProviderRegistrar {

    @Override
    public Optional<ChatRequest.Builder<?>> createChatRequestBuilder() {
        return Optional.of(new OpenAIChatRequest.Builder());
    }

    @Override
    public Optional<ChatModel> createChatModel(ModelConfigAggregator configAggregator) {
        return Optional.of(OpenAIChatModel.builder())
                // 模型 Config 配置
                .map(builder -> {
                    setIfPresent(builder::apiUrl, configAggregator.getApiUrl(), OpenAIConstant.DEFAULT_API_URL);
                    setIfPresent(builder::endPoint, configAggregator.getEndPoint(), OpenAIConstant.DEFAULT_END_POINT);
                    setIfPresent(builder::apiKey, configAggregator.getApiKey());
                    setIfPresent(builder::provider, configAggregator.getProvider(), OpenAIConstant.PROVIDER_NAME);
                    setIfPresent(builder::model, configAggregator.getModel());
                    setIfPresent(builder::connectTimeout, configAggregator.getConnectTimeout());
                    setIfPresent(builder::readTimeout, configAggregator.getReadTimeout());
                    setIfPresent(builder::headersConfig, configAggregator.getCustomHeaders());
                    setIfPresent(builder::autoToolCallEnabled, configAggregator.getAutoToolCallEnabled().toBool());
                    return builder;
                })
                // 构建模型
                .map(OpenAIChatModel.Builder::build);
    }

    @Override
    public String getProviderName() {
        return OpenAIConstant.PROVIDER_NAME;
    }
}
