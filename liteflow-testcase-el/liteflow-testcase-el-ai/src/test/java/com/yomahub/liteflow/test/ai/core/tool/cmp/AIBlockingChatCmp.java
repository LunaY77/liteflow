package com.yomahub.liteflow.test.ai.core.tool.cmp;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.AIInput;
import com.yomahub.liteflow.ai.annotation.InputField;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.util.TriState;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@AIComponent(
        nodeId = "aiBlockingChatCmpId",
        nodeName = "aiBlockingChatCmpName",
//        provider = "ollama",
//        apiUrl = "http://localhost:11434",
//        model = "qwen3:32b",
//        provider = "dashscope",
//        apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
//        model = "deepseek-r1",
        provider = "openai",
        apiUrl = "https://ark.cn-beijing.volces.com/api/v3",
        model = "doubao-seed-1-6-250615",
        enableThinking = TriState.FALSE,
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
public interface AIBlockingChatCmp {
}
