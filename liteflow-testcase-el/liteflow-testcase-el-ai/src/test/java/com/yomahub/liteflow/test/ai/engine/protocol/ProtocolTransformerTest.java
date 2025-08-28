package com.yomahub.liteflow.test.ai.engine.protocol;

import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.engine.interact.protocol.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.model.openai.interact.OpenAIProtocolTransformer;
import com.yomahub.liteflow.test.ai.engine.protocol.domain.ResponseDataMock;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class ProtocolTransformerTest {

    @Test
    public void testOpenAI() {
        List<String> streamChunks = ResponseDataMock.getOpenAIStreamChunks();
        ProtocolTransformer transformer = new OpenAIProtocolTransformer();
        InteractContext context = new InteractContext();
        for (String streamChunk : streamChunks) {
            StreamingProtocolChunk protocolChunk = transformer.transformStreamingChunk(streamChunk, context);
            System.out.println(protocolChunk.getData());
        }
        ChatResponse chatResponse = transformer.transformStreamingResponse(context);
        System.out.println("==========================");
        System.out.println(chatResponse.hasToolCalls());
        System.out.println(chatResponse.getOutput().getContent());
        System.out.println(chatResponse.getTokenUsage());
        System.out.println(chatResponse.getFinishReason());

        for (ToolCall toolCall : chatResponse.getOutput().getToolCalls()) {
            System.out.println(toolCall.getId());
            System.out.println(toolCall.getName());
            System.out.println(toolCall.getType());
            System.out.println(toolCall.getArguments());
        }
    }

}
