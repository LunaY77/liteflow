package com.yomahub.liteflow.ai.model.ollama.model;

import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.ModelProviderRegistrar;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatRequest;

import java.util.Optional;

import static com.yomahub.liteflow.ai.util.SetUtil.setIfPresent;

/**
 * Ollama 模型提供者
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class OllamaModelProvider extends ModelProviderRegistrar {

    @Override
    public Optional<ChatRequest.Builder<?>> createChatRequestBuilder() {
        return Optional.of(OllamaChatRequest.builder());
    }

    @Override
    public Optional<ChatModel> createChatModel(ModelConfigAggregator configAggregator) {
        return Optional.of(OllamaChatModel.builder())
                // 模型 Config 配置
                .map(builder -> {
                    setIfPresent(builder::apiUrl, configAggregator.getApiUrl(), OllamaConstant.DEFAULT_API_URL);
                    setIfPresent(builder::endPoint, configAggregator.getEndPoint(), OllamaConstant.DEFAULT_END_POINT);
                    setIfPresent(builder::apiKey, configAggregator.getApiKey());
                    setIfPresent(builder::provider, configAggregator.getProvider(), OllamaConstant.PROVIDER_NAME);
                    setIfPresent(builder::model, configAggregator.getModel());
                    setIfPresent(builder::connectTimeout, configAggregator.getConnectTimeout());
                    setIfPresent(builder::readTimeout, configAggregator.getReadTimeout());
                    setIfPresent(builder::headersConfig, configAggregator.getCustomHeaders());
                    setIfPresent(builder::autoToolCallEnabled, configAggregator.getAutoToolCallEnabled().toBool());
                    setIfPresent(builder::logRequest, configAggregator.getLogRequests());
                    setIfPresent(builder::logResponse, configAggregator.getLogResponses());
                    return builder;
                })
                // 构建模型
                .map(OllamaChatModel.Builder::build);
    }

    @Override
    public String getProviderName() {
        return OllamaConstant.PROVIDER_NAME;
    }
}
