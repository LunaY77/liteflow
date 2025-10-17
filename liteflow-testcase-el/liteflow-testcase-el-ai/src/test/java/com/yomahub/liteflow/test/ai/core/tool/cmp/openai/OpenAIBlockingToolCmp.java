package com.yomahub.liteflow.test.ai.core.tool.cmp.openai;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;

@AIComponent(
        nodeId = "openaiBlockingToolCmpId",
        nodeName = "openaiBlockingToolCmpName",
        provider = ProviderEnum.OPENAI,
        apiUrl = "https://ark.cn-beijing.volces.com/api/v3",
        model = "doubao-seed-1-6-250615",
        enableThinking = false,
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
public interface OpenAIBlockingToolCmp {
}
