package com.yomahub.liteflow.test.ai.core.classify.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        System.out.println("ACmp executed!");
    }
}
