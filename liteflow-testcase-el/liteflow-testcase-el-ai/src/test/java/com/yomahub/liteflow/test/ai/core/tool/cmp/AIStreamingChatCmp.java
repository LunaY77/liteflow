package com.yomahub.liteflow.test.ai.core.tool.cmp;

import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
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
        enableThinking = TriState.TRUE,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        userPrompt = "{{question}}",
        transportType = TransportType.DnJson,
        toolNames = "assemble_tool"
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "调用工具组装 QQ 和 微信"),
        }
)
public interface AIStreamingChatCmp {
}
