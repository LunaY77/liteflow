package com.yomahub.liteflow.test.ai.core.tool.cmp.dashscope;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;

@AIComponent(
        nodeId = "dashscopeBlockingToolCmpId",
        nodeName = "dashscopeBlockingToolCmpName",
        provider = ProviderEnum.DASHSCOPE,
        apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
        model = "qwen-flash",
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
public interface DashScopeBlockingToolCmp {
}
