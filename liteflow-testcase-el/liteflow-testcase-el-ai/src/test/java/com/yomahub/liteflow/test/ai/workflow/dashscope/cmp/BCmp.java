package com.yomahub.liteflow.test.ai.workflow.dashscope.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("b")
public class BCmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        WorkFlowContext context = this.getContextBean(WorkFlowContext.class);
        System.out.println("BCmp executed! The result is: " + context.getResult().getOutput().getText());
    }
}
