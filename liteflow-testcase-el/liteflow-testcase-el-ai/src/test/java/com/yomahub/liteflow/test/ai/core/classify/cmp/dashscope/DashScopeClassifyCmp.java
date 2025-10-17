package com.yomahub.liteflow.test.ai.core.classify.cmp.dashscope;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;

@AIComponent(
        nodeId = "dashscopeSwitch",
        nodeName = "dashscopeSwitch",
        provider = ProviderEnum.DASHSCOPE,
        apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
        model = "qwen-flash",
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
        methodExpress = "setData",
        useKeyIndex = true,
        key = "result"
)
public interface DashScopeClassifyCmp {
}
