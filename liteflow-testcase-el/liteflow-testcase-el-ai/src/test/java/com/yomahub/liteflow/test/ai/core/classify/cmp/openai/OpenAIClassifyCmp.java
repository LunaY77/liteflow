package com.yomahub.liteflow.test.ai.core.classify.cmp.openai;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;

@AIComponent(
        nodeId = "openaiSwitch",
        nodeName = "openaiSwitch",
        provider = ProviderEnum.OPENAI,
        apiUrl = "https://ark.cn-beijing.volces.com/api/v3",
        model = "doubao-seed-1-6-250615",
        enableThinking = true,
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
public interface OpenAIClassifyCmp {
}
