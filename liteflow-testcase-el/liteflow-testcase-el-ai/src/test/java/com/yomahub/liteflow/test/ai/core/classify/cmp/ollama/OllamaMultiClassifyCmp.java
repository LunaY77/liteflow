package com.yomahub.liteflow.test.ai.core.classify.cmp.ollama;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.util.TriState;

@AIComponent(
        nodeId = "ollamaMultiSwitch",
        nodeName = "ollamaMultiSwitch",
        provider = ProviderEnum.OLLAMA,
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        enableThinking = false,
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
public interface OllamaMultiClassifyCmp {
}
