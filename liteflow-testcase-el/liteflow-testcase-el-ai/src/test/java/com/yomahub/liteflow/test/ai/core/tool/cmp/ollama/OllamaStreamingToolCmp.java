package com.yomahub.liteflow.test.ai.core.tool.cmp.ollama;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;

@AIComponent(
        nodeId = "ollamaStreamingToolCmpId",
        nodeName = "ollamaStreamingToolCmpName",
        provider = ProviderEnum.OLLAMA,
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        enableThinking = true,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        userPrompt = "{{question}}",
        transportType = TransportType.DN_JSON,
        toolNames = "assemble_tool"
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "调用工具组装 QQ 和 微信"),
        }
)
public interface OllamaStreamingToolCmp {
}
