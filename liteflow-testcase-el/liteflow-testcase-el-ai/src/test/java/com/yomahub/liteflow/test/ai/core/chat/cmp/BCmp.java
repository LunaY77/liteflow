package com.yomahub.liteflow.test.ai.core.chat.cmp;

import com.yomahub.liteflow.ai.engine.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@Component("b")
public class BCmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        AssistantMessage assistantMessage = this.getContextValue("dataMap.result");
        System.out.println(assistantMessage.getContent());
    }
}
