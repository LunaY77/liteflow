package com.yomahub.liteflow.test.ai.core.classify.cmp;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.util.TriState;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@AIComponent(
        nodeId = "aiMultiSwitch",
        nodeName = "aiMultiSwitch",
//        provider = "ollama",
//        apiUrl = "http://localhost:11434",
//        model = "qwen3:32b",
        provider = "openai",
        apiUrl = "https://ark.cn-beijing.volces.com/api/v3",
        model = "doubao-seed-1-6-250615",
        enableThinking = TriState.FALSE,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIClassify(
        userPrompt = "{{question}}",
        categories = {"java", "python"},
        multiLabel = true
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "请帮我写一段Java代码, 同时给出 Python 代码"),
        }
)
@AIOutput(
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface AIMultiClassifyCmp {
}
