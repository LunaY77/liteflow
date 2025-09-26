package com.yomahub.liteflow.test.ai.core.chat.cmp.ollama;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.util.TriState;

@AIComponent(
        nodeId = "OllamaChat",
        nodeName = "OllamaChat",
        provider = "ollama",
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        enableThinking = TriState.FALSE,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        systemPrompt = "classpath:core/chat/system_prompt.txt",
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
        typeName = "com.yomahub.liteflow.test.ai.core.chat.cmp.Output",
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface OllamaChatCmp {
}
