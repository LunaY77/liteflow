package com.yomahub.liteflow.test.ai.core.chat.cmp.openai;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;


@AIComponent(
        nodeId = "OpenAIStream",
        nodeName = "OpenAIStream",
        provider = ProviderEnum.OPENAI,
        apiUrl = "https://ark.cn-beijing.volces.com/api/v3",
        model = "doubao-seed-1-6-250615",
        enableThinking = true,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        systemPrompt = "classpath:core/chat/system_prompt.txt",
        userPrompt = "{{question}}",
        streaming = true,
        transportType = TransportType.SSE
)
@AIInput(
        mapping = {
                @InputField(name = "question",  expression = "test", defaultValue = "简短讲解什么是 LiteFlow？"),
        }
)
@AIOutput(
        responseType = ResponseType.TEXT,
        typeName = "com.yomahub.liteflow.test.ai.core.chat.cmp.Output",
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface OpenAIStreamCmp {
}
