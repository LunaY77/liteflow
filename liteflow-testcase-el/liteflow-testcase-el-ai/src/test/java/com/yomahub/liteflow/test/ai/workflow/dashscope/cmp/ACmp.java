package com.yomahub.liteflow.test.ai.workflow.dashscope.cmp;

import com.google.gson.JsonObject;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        WorkFlowContext context = this.getContextBean(WorkFlowContext.class);
        JsonObject bizParams = new JsonObject();
        bizParams.addProperty("city", "beijing");
        context.setBizParams(bizParams);
        System.out.println("ACmp executed!");
    }
}
