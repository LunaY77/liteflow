package com.yomahub.liteflow.test.ai.engine.structure.param;

import com.yomahub.liteflow.ai.engine.model.output.structure.Description;

import java.util.List;

/**
 * output with t
 *
 * @author 苍镜月
 */

public class OutputWithT<T> {

    @Description("a data description")
    private T data;
    @Description("a list description")
    private List<Integer> lst;

    public T getData() {
        return data;
    }

    public List<Integer> getLst() {
        return lst;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setLst(List<Integer> lst) {
        this.lst = lst;
    }
}
