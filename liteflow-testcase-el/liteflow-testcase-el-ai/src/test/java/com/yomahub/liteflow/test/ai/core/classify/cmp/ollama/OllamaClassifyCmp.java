package com.yomahub.liteflow.test.ai.core.classify.cmp.ollama;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;

@AIComponent(
        nodeId = "ollamaSwitch",
        nodeName = "ollamaSwitch",
        provider = ProviderEnum.OLLAMA,
        apiUrl = "http://localhost:11434",
        model = "qwen3:32b",
        enableThinking = false,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIClassify(
        userPrompt = "{{question}}",
        categories = {"java", "python"}
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "请帮我写一段Java代码"),
        }
)
@AIOutput(
        methodExpress = "setData(\"result\", $result)"
)
public interface OllamaClassifyCmp {
}
