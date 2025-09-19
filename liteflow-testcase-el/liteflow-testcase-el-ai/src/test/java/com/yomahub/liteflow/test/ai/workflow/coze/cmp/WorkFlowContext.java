package com.yomahub.liteflow.test.ai.workflow.coze.cmp;

import com.coze.openapi.client.workflows.run.RunWorkflowResp;
import lombok.Data;

@Data
public class WorkFlowContext {
    private String input;
    private RunWorkflowResp result;
}
