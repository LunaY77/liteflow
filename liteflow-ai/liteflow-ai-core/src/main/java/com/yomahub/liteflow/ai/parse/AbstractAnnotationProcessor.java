package com.yomahub.liteflow.ai.parse;

import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;

/**
 * 抽象粉色奶龙处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractAnnotationProcessor<A extends Annotation, T extends AIProxyWrapBean<A>>
        implements AnnotationProcessor<A, T>, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationParser.register(getAIType().getCode(), this);
    }

    /**
     * 获取对应的 AI 节点类型
     *
     * @return {@link AITypeEnum}
     */
    protected abstract AITypeEnum getAIType();
}
