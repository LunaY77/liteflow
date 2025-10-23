package com.yomahub.liteflow.test.ai.core.structure.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.test.ai.core.structure.output.MathReasoning;
import org.springframework.stereotype.Component;

@Component("b")
public class BCmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        MathReasoning output = this.getContextValue("dataMap.result");
        System.out.println("BCmp executed! The math reasoning output is: \n" + output);
    }
}
