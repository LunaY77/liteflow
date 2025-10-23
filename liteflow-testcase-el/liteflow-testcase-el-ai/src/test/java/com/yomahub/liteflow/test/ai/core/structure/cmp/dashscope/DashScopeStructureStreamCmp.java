package com.yomahub.liteflow.test.ai.core.structure.cmp.dashscope;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.annotation.model.io.InputField;
import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;

/**
 * 阿里百炼平台结构化输出测试
 * <p>
 * 请注意！！！在测试代码中，该组件仅作为展示作用，不会真正执行调用AI服务接口，
 * 而是使用 mock 数据进行测试，故注解中的配置信息大部分不生效，仅作为参考示例！！！
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@AIComponent(
        nodeId = "DashScopeStructureStream",
        nodeName = "DashScopeStructureStream",
        provider = ProviderEnum.DASHSCOPE,
//        apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
        model = "deepseek-r1",
        enableThinking = false,
        readTimeout = "10m",
        connectTimeout = "10m"
)
@AIChat(
        systemPrompt = "你是一位数学辅导老师",
        userPrompt = "{{question}}",
        streaming = true,
        transportType = TransportType.SSE
)
@AIInput(
        mapping = {
                @InputField(name = "question", expression = "test", defaultValue = "使用中文解题: 8x + 9 = 32 and x + y = 1"),
        }
)
@AIOutput(
        responseType = ResponseType.JSON,
        typeName = "com.yomahub.liteflow.test.ai.core.structure.output.MathReasoning",
        methodExpress = "setData(\"result\", $result)"
)
public interface DashScopeStructureStreamCmp {
}
