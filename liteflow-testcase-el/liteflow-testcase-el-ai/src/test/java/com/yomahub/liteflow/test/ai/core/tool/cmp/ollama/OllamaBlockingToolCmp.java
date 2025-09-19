package com.yomahub.liteflow.test.ai.core.tool.cmp.ollama;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.util.TriState;

@AIComponent(
        nodeId = "ollamaBlockingToolCmpId",
        nodeName = "ollamaBlockingToolCmpName",
        provider = "ollama",
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        enableThinking = TriState.FALSE,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        userPrompt = "{{question}}",
        streaming = false,
        transportType = TransportType.HTTP,
        toolNames = {"weather_tool"}
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "nothing", defaultValue = "北京今天天气怎么样")
        }
)
public interface OllamaBlockingToolCmp {
}
