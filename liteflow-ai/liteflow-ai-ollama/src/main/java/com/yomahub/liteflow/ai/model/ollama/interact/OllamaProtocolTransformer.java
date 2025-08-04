package com.yomahub.liteflow.ai.model.ollama.interact;

import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;

import static com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant.PROVIDER_NAME;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaProtocolTransformer implements ProtocolTransformer {

    @Override
    public StreamingProtocolChunk transformStreamingChunk(String streamChunk, InteractContext context) {
        return null;
    }

    @Override
    public ChatResponse transformStreamingResponse(InteractContext context) {
        return null;
    }

    @Override
    public ChatResponse transformBlockingResponse(String blockingResponse, InteractContext context) {
        AssistantMessage message = new AssistantMessage(blockingResponse);
        return new ChatResponse(message);
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
}
