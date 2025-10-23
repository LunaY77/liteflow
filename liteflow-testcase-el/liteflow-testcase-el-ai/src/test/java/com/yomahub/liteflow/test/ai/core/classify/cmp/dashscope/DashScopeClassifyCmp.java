package com.yomahub.liteflow.test.ai.core.classify.cmp.dashscope;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;

/**
 * 阿里百炼平台 Classify 组件测试
 * <p>
 * 请注意！！！在测试代码中，该组件仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故注解中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */

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
        methodExpress = "setData(\"result\", $result)"
)
public interface DashScopeClassifyCmp {
}
