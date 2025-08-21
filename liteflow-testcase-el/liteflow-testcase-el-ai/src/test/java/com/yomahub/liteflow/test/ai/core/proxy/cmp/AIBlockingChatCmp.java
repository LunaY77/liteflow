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
        nodeId = "aiBlockingChatCmpId",
        nodeName = "aiBlockingChatCmpName",
        provider = "ollama",
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        enableThinking = TriState.FALSE,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        systemPrompt = "classpath:core/proxy/system_prompt.txt",
        userPrompt = "{{question}}",
        streaming = false,
        transportType = TransportType.HTTP
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "What is LiteFlow?"),
        }
)
@AIOutput(
        responseType = ResponseType.JSON,
        typeName = "com.yomahub.liteflow.test.ai.core.proxy.cmp.Output",
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface AIBlockingChatCmp {
}
