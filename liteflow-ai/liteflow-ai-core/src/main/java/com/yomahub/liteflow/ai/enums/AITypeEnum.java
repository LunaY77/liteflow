package com.yomahub.liteflow.ai.enums;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 节点类型枚举
 *
 * @author 苍镜月
 * @since TODO
 */

public enum AITypeEnum {
    /**
     * AI对话 - 对应 NodeComponent
     */
    CHAT("AIChat", NodeComponent.class),

    /**
     * AI分类 - 对应 NodeSwitchComponent
     */
    CLASSIFY("AIClassify", NodeSwitchComponent.class),

    /**
     * AI检索 - 对应 NodeComponent
     */
    RETRIEVAL("AIRetrieval", NodeComponent.class);

    private final String type;
    private final Class<? extends NodeComponent> componentClass;

    private static final Map<String, Class<? extends NodeComponent>> cache;

    AITypeEnum(String type, Class<? extends NodeComponent> componentClass) {
        this.type = type;
        this.componentClass = componentClass;
    }

    public String getType() {
        return type;
    }

    public Class<? extends NodeComponent> getComponentClass() {
        return componentClass;
    }

    static {
        cache = Arrays.stream(AITypeEnum.values()).collect(Collectors.toMap(AITypeEnum::getType, AITypeEnum::getComponentClass));
    }

    /**
     * 根据注解获取对应的 NodeComponent 类
     * @param annotationType 注解类
     * @return 对应的 NodeComponent 类
     */
    public static Class<? extends NodeComponent> fromAnnotationType(Class<?> annotationType) {
        return cache.get(annotationType.getSimpleName());
    }
}
