package com.yomahub.liteflow.test.cmp;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.annotation.AIComponent;

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
        userPrompt = "Why sky is blue?"
)
public interface AICmp {
}
