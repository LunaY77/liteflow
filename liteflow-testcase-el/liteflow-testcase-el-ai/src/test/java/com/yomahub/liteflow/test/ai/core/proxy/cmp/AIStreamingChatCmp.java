package com.yomahub.liteflow.test.ai.core.proxy.cmp;

import com.yomahub.liteflow.ai.annotation.*;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.util.TriState;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@AIComponent(
        nodeId = "aiStreamingChatCmpId",
        nodeName = "aiStreamingChatCmpName",
        provider = "ollama",
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        apiKey = "",
        enableThinking = TriState.TRUE,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        systemPrompt = "classpath:core/proxy/system_prompt.txt",
        userPrompt = "{{question}}",
        streaming = true,
        transportType = TransportType.DnJson
)
@AIInput(
        mapping = {
                @InputField(name = "question",  expression = "test", defaultValue = "简短讲解什么是 LiteFlow？"),
        }
)
@AIOutput(
        responseType = ResponseType.TEXT,
        typeName = "com.yomahub.liteflow.test.ai.core.proxy.cmp.Output",
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface AIStreamingChatCmp {
}
