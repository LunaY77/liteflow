package com.yomahub.liteflow.flow.element.condition;

import com.yomahub.liteflow.flow.element.Condition;

/**
 * 抽象并行条件
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractParallelCondition extends Condition {
    //判断循环是否并行执行，默认为false
    private boolean parallel = false;

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }
}
