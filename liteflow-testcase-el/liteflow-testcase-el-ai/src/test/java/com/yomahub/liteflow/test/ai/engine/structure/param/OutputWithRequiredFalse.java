package com.yomahub.liteflow.test.ai.engine.structure.param;

import com.yomahub.liteflow.ai.engine.model.output.structure.Description;

/**
 * OutputWithRequiredFalse
 *
 * @author 苍镜月
 */

public class OutputWithRequiredFalse {

    @Description(value = "a data description", required = false)
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
