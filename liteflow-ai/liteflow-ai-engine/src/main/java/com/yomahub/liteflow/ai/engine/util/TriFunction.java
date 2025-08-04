package com.yomahub.liteflow.ai.engine.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * 三元函数接口，接受三个参数并返回一个结果。
 *
 * @author 苍镜月
 * @since TODO
 */

@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * 返回一个新的函数，该函数将当前函数的结果应用于给定的参数，并将结果传递给指定的后续函数。
     *
     * @param after 后续函数
     * @param <W>   后续函数的返回类型
     * @return 返回一个新的函数
     */
    default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (t, u, v) -> after.apply(apply(t, u, v));
    }

    /**
     * 应用函数
     *
     * @param t 第一个参数
     * @param u 第二个参数
     * @param v 第三个参数
     * @return 返回结果
     */
    R apply(T t, U u, V v);
}
