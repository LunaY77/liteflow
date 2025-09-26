package com.yomahub.liteflow.test.ai.engine.structure.param;

import com.yomahub.liteflow.ai.engine.model.output.structure.Description;

import java.util.List;

/**
 * output
 *
 * @author 苍镜月
 */

public class Output {
    @Description("a data description")
    private String data;
    @Description("a list description")
    private List<Integer> lst;

    public String getData() {
        return data;
    }

    public List<Integer> getLst() {
        return lst;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setLst(List<Integer> lst) {
        this.lst = lst;
    }
}
