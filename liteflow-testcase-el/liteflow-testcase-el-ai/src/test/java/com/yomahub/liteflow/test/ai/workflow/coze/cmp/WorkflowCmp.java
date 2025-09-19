package com.yomahub.liteflow.test.ai.workflow.coze.cmp;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.util.KeyValue;
import com.yomahub.liteflow.ai.workflow.coze.annotation.CozeWorkflowRun;

@AIComponent(
        nodeId = "cozeWorkflowNode",
        nodeName = "Coze-Workflow"
)
@CozeWorkflowRun(
        workflowId = "7547589243724939314",
        parameters = {
                @KeyValue(key = "input", value = "{{input}}")
        },
        appId = "7547538064139485230"
)
@AIOutput(
        methodExpress = "setResult"
)
public interface WorkflowCmp {
}
