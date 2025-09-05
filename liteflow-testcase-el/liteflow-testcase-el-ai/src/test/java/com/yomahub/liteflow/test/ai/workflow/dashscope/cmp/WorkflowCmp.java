package com.yomahub.liteflow.test.ai.workflow.dashscope.cmp;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.workflow.dashscope.annotation.DashScopeWorkflow;

@AIComponent(
        nodeId = "dsWorkflowNode",
        nodeName = "DashScope-Workflow"
)
@DashScopeWorkflow(
        appId = "dfa713dda47146b98e296dcb9174bf28",
        prompt = "I want to know the weather in Beijing, please help me find it.",
        bizParams = "{{bizParams}}"
)
@AIOutput(
        methodExpress = "setResult"
)
public interface WorkflowCmp {
}
