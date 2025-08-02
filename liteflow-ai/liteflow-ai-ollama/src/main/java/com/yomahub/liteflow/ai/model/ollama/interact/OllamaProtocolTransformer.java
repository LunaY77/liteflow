package com.yomahub.liteflow.ai.model.ollama.interact;

import com.yomahub.liteflow.ai.engine.interact.pipeline.ChatContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformerRegistrar;
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

public class OllamaProtocolTransformer extends ProtocolTransformerRegistrar implements ProtocolTransformer {

    @Override
    public StreamingProtocolChunk transformStreamingChunk(String streamChunk, ChatContext context) {
        return null;
    }

    @Override
    public ChatResponse transformStreamingResponse(ChatContext context) {
        return null;
    }

    @Override
    public ChatResponse transformBlockingResponse(String blockingResponse, ChatContext context) {
        AssistantMessage message = new AssistantMessage(blockingResponse);
        return new ChatResponse(message, context.getChatId(), true);
    }

    @Override
    protected String getProviderName() {
        return PROVIDER_NAME;
    }
}
