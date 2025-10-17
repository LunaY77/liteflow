package com.yomahub.liteflow.test.ai.core.tool.cmp.openai;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;

@AIComponent(
        nodeId = "openaiStreamingToolCmpId",
        nodeName = "openaiStreamingToolCmpName",
        provider = ProviderEnum.OPENAI,
        apiUrl = "https://ark.cn-beijing.volces.com/api/v3",
        model = "doubao-seed-1-6-250615",
        enableThinking = true,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        userPrompt = "{{question}}",
        transportType = TransportType.SSE,
        toolNames = "assemble_tool"
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "调用工具组装 QQ 和 微信"),
        }
)
public interface OpenAIStreamingToolCmp {
}
