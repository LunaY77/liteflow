package com.yomahub.liteflow.ai.domain.enums;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * AI 节点类型枚举
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public enum AITypeEnum {
    /**
     * AI对话 - 对应 NodeComponent
     */
    CHAT(1, "AIChat", NodeComponent.class),

    /**
     * AI分类 - 对应 NodeSwitchComponent
     */
    CLASSIFY(2, "AIClassify", NodeSwitchComponent.class),

    /**
     * AI工作流 - 对应 NodeComponent
     */
    WORKFLOW(3, "AIWorkflow", NodeComponent.class);

    private final Integer code;
    private final String type;
    private final Class<? extends NodeComponent> componentClass;

    private static final Map<String, AITypeEnum> cache;

    AITypeEnum(Integer code, String type, Class<? extends NodeComponent> componentClass) {
        this.code = code;
        this.type = type;
        this.componentClass = componentClass;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public Class<? extends NodeComponent> getComponentClass() {
        return componentClass;
    }

    static {
        cache = Arrays.stream(AITypeEnum.values()).collect(Collectors.toMap(AITypeEnum::getType, Function.identity()));
    }

    /**
     * 根据注解获取对应的 {@link AITypeEnum} 类
     *
     * @param annotation 注解
     * @return {@link AITypeEnum}
     */
    public static AITypeEnum fromAnnotationType(Annotation annotation) {
        return Optional.ofNullable(
                cache.get(
                        // 通过反射获取到的注解类名为代理类名，需要进行特殊处理
                        Arrays.stream(annotation.getClass().getInterfaces())
                                .filter(Annotation.class::isAssignableFrom)
                                .findAny()
                                .map(Class::getSimpleName)
                                .orElseGet(() -> annotation.getClass().getSimpleName()))
        // 如果没有找到对应的类型，可能是 WORKFLOW 类型
        ).orElse(AITypeEnum.WORKFLOW);
    }
}
