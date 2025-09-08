package com.yomahub.liteflow.builder.el.operator;

import com.yomahub.liteflow.builder.el.operator.base.BaseOperator;
import com.yomahub.liteflow.builder.el.operator.base.OperatorHelper;
import com.yomahub.liteflow.flow.element.condition.AbstractParallelCondition;

/**
 * EL规则中的parallel的操作符
 *
 * @author zhhhhy
 * @since 2.11.0
 */

public class ParallelOperator extends BaseOperator<AbstractParallelCondition> {
    @Override
    public AbstractParallelCondition build(Object[] objects) throws Exception {
        OperatorHelper.checkObjectSizeEqTwo(objects);

        String errorMsg = "The caller must be AbstractParallelCondition item";
        AbstractParallelCondition parallelCondition = OperatorHelper.convert(objects[0], AbstractParallelCondition.class, errorMsg);

        Boolean parallel = OperatorHelper.convert(objects[1], Boolean.class);
        parallelCondition.setParallel(parallel);
        return parallelCondition;
    }
}
