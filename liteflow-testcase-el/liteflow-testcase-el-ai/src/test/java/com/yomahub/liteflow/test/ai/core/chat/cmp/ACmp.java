package com.yomahub.liteflow.test.ai.core.chat.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        System.out.println("ACmp executed!");
    }
}
