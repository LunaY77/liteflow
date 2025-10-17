package com.yomahub.liteflow.test.ai.core.chat.cmp.dashscope;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;

@AIComponent(
        nodeId = "DashScopeChat",
        nodeName = "DashScopeChat",
        provider = ProviderEnum.DASHSCOPE,
//        apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
        model = "qwen-flash",
        enableThinking = false,
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
public interface DashScopeChatCmp {
}
