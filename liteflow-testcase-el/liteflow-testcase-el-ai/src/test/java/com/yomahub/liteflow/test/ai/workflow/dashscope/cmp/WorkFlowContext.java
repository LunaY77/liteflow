package com.yomahub.liteflow.test.ai.workflow.dashscope.cmp;

import com.alibaba.dashscope.app.ApplicationResult;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class WorkFlowContext {
    private JsonObject bizParams;
    private ApplicationResult result;
}
