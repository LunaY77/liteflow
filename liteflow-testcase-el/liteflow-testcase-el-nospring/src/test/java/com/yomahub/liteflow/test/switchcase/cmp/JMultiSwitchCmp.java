package com.yomahub.liteflow.test.switchcase.cmp;

import com.yomahub.liteflow.core.NodeSwitchComponent;

public class JMultiSwitchCmp extends NodeSwitchComponent {

    @Override
    public String processSwitch() throws Exception {
        return "tag:tag1, tag:tag2, d";
    }
}
