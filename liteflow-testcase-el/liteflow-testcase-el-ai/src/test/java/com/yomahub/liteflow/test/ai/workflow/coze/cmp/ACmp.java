package com.yomahub.liteflow.test.ai.workflow.coze.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        WorkFlowContext context = this.getContextBean(WorkFlowContext.class);
        context.setInput("What is the capital of France?");
        System.out.println("ACmp executed!");
    }
}
