package com.yomahub.liteflow.test.ai.core.chat.cmp;

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
        Output res = this.getContextValue("dataMap.result");
        System.out.println(res.getContent());
    }
}
