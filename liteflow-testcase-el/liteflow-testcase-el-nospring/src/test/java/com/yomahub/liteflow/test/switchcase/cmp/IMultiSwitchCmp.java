package com.yomahub.liteflow.test.switchcase.cmp;

import com.yomahub.liteflow.core.NodeSwitchComponent;

public class IMultiSwitchCmp extends NodeSwitchComponent {

    @Override
    public String processSwitch() throws Exception {
        return "a, b, c";
    }
}
