package com.yomahub.liteflow.ai.engine.model.output.structure;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 动态构建泛型
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ParameterizedTypeImpl implements ParameterizedType {

    private final Type rawType;
    private final Type[] actualTypeArguments;

    public ParameterizedTypeImpl(Type rawType, Type... actualTypeArguments) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public @NotNull Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @NotNull
    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        Class<?> raw = (Class) this.rawType;
        buf.append(raw.getName());
        appendAllTo(buf.append('<'), ", ", this.actualTypeArguments).append('>');
        return buf.toString();
    }

    private static StringBuilder appendAllTo(StringBuilder buf, String sep, Type... types) {
        if (ArrayUtil.isNotEmpty(types)) {
            boolean isFirst = true;

            for (Type type : types) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    buf.append(sep);
                }

                String typeStr;
                if (type instanceof Class) {
                    typeStr = ((Class) type).getName();
                } else {
                    typeStr = StrUtil.toString(type);
                }

                buf.append(typeStr);
            }
        }

        return buf;
    }
}
