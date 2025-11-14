package com.yomahub.liteflow.test.ai.model.util;

import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.interact.chunk.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import io.reactivex.rxjava3.functions.Consumer;

public class StreamUtil {

    public static Consumer<ChunkEvent> getChunkEventConsumer() {
        return chunkEvent -> {
            if (chunkEvent.isStart()) {
                System.out.println("chat start");
            }
            if (chunkEvent.isChunk()) {
                StreamingProtocolChunk chunk = chunkEvent.getTransformedChunk();
                switch (chunk.getType()) {
                    case TEXT:
                        System.out.println(chunk.getData());
                        break;
                    case THINKING:
                        System.out.println("[Thinking] " + chunk.getData());
                }
            }
            if (chunkEvent.isComplete()) {
                ChatResponse response = chunkEvent.getFinalResponse();
                AssistantMessage message = response.getOutput();
                if (message.getContent() != null && !message.getContent().trim().isEmpty()) {
                    System.out.println("内容长度: " + message.getContent().length());
                    if (message.getContent().length() > 200) {
                        System.out.println("内容预览: " + message.getContent().substring(0, 200) + "...");
                    } else {
                        System.out.println("内容: " + message.getContent());
                    }
                }
                if (response.hasToolCalls()) {
                    System.out.println("工具调用数量: " + message.getToolCalls().size());
                    for (int i = 0; i < message.getToolCalls().size(); i++) {
                        ToolCall toolCall = message.getToolCalls().get(i);
                        System.out.println("工具调用 " + (i + 1) + ":");
                        System.out.println("  ID: " + toolCall.getId());
                        System.out.println("  名称: " + toolCall.getName());
                        System.out.println("  类型: " + toolCall.getType());
                        System.out.println("  参数: " + toolCall.getArguments());
                    }
                }
                System.out.println("Token使用情况: " + response.getTokenUsage());
                System.out.println("完成原因: " + response.getFinishReason());
                System.out.println("chat close");
            }
        };
    }
}
