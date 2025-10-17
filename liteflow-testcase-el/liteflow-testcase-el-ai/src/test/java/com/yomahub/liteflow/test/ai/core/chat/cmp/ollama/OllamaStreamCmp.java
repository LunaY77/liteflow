package com.yomahub.liteflow.test.ai.core.chat.cmp.ollama;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.util.TriState;

@AIComponent(
        nodeId = "OllamaStream",
        nodeName = "OllamaStream",
        provider = ProviderEnum.OLLAMA,
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        enableThinking = true,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        systemPrompt = "classpath:core/chat/system_prompt.txt",
        userPrompt = "{{question}}",
        streaming = true,
        transportType = TransportType.DnJson
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "简短讲解什么是 LiteFlow？"),
        }
)
@AIOutput(
        responseType = ResponseType.TEXT,
        typeName = "com.yomahub.liteflow.test.ai.core.chat.cmp.Output",
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface OllamaStreamCmp {
}
