package com.yomahub.liteflow.test.ai.proxy.cmp;

import com.yomahub.liteflow.ai.annotation.*;
import com.yomahub.liteflow.ai.domain.enums.ResponseType;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@AIComponent(
        nodeId = "aiCmpId",
        nodeName = "aiCmpName",
        provider = "ollama",
        baseUrl = "http://localhost:11434",
        model = "qwen3:32b"
)
@AIChat(
        systemPrompt = "classpath:proxy/system_prompt.txt",
        userPrompt = "{{question}}, {{answer}}"
)
@AIInput(
        mapping = {
                @InputField(name = "question",  expression = "test", defaultValue = "Why sky is blue?"),
                @InputField(name = "answer", expression = "test", defaultValue = "The sky appears blue due to the scattering of sunlight by the atmosphere.")
        }
)
@AIOutput(
        responseType = ResponseType.JSON,
        entityClass = Output.class,
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface AICmp {
}
