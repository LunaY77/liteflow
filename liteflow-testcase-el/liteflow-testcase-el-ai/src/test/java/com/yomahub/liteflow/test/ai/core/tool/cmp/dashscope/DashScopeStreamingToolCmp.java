package com.yomahub.liteflow.test.ai.core.tool.cmp.dashscope;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.util.TriState;

@AIComponent(
        nodeId = "dashscopeStreamingToolCmpId",
        nodeName = "dashscopeStreamingToolCmpName",
        provider = "dashscope",
        apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
        model = "qwen-flash",
        enableThinking = TriState.TRUE,
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
public interface DashScopeStreamingToolCmp {
}
