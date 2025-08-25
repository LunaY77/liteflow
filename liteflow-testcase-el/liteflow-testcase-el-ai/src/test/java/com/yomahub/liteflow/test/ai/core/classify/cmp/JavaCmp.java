package com.yomahub.liteflow.test.ai.core.classify.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Component("java")
public class JavaCmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        System.out.println("Java component executed.");
    }
}
