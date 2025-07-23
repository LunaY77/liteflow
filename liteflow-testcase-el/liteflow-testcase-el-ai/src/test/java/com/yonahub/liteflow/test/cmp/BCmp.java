package com.yonahub.liteflow.test.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Component("b")
public class BCmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        System.out.println("BCmp executed!");
    }
}
