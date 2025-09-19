package com.yomahub.liteflow.ai.engine.model.output.structure;

import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 保存泛型信息，绕开 java 泛型擦除
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class TypeReference<T> {

    private final Type type;

    /**
     * 构造函数，获取当前类的泛型类型
     */
    protected TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new RuntimeException("TypeReference must be a parameterized type");
        } else {
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    /**
     * 构造函数，使用类全限定名表示的类型名称
     *
     * @param typeName 类全限定名
     */
    public TypeReference(String typeName) {
        this.type = JsonSchemaGenerator.typeFromString(typeName);
    }

    public Type getType() {
        return type;
    }
}
