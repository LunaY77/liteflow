package com.yomahub.liteflow.test.cmp;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.AIInput;
import com.yomahub.liteflow.ai.annotation.InputField;

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
        systemPrompt = "classpath:system_prompt.txt",
        userPrompt = "{{question}}, {{answer}}"
)
@AIInput(
        mapping = {
                @InputField(name = "question",  expression = "test", defaultValue = "Why sky is blue?"),
                @InputField(name = "answer", expression = "test", defaultValue = "The sky appears blue due to the scattering of sunlight by the atmosphere.")
        }
)
public interface AICmp {
}
