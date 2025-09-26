package com.yomahub.liteflow.ai.annotation.model.io;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 输出字段映射规则。
 * <p>
 * 用于将结构化输出对象 ({@code entityClass}) 中的某一个字段，
 * 精确映射到 LiteFlow 上下文的指定位置。
 * 此注解应在 {@link AIOutput#mapping()} 数组中使用。
 *
 * @author 苍镜月
 * @since 2.16.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OutputField {

    /**
     * 源字段名称（必需）。
     * <p>
     * 指定要从结构化输出对象 ({@link AIOutput#typeName()}) 中读取的字段名。
     */
    String sourceField();

    /**
     * 目标方法表达式（可选）。
     * <p>
     * 用于将此字段的值设置到上下文中。
     * <b>如果留空（默认），则会继承父注解 {@link AIOutput} 中的 {@code methodExpress()} 设置。</b>
     */
    String methodExpress() default "";

    /**
     * 是否启用“按键名/索引”的映射策略（默认为 false）。
     * <p>
     * 当为 {@code true} 时，框架将使用此注解内的 {@code key()} 或 {@code index()} 的值。
     * <p>
     * 当为 {@code false} 时（默认），此字段的值将直接作为参数调用 {@code methodExpress()} 指定的方法。
     */
    boolean useKeyIndex() default false;

    /**
     * 键名（可选），用于向 Map 类型的目标输出数据。
     * <p>
     * <b>注意：</b>此参数仅在当前注解的 {@code useKeyIndex()} 为 {@code true} 时生效。
     * 它与 {@code index()} 参数互斥。
     *
     * @see #useKeyIndex()
     */
    String key() default "";

    /**
     * 索引（可选），用于向 List 或数组类型的目标输出数据。
     * <p>
     * <b>注意：</b>此参数仅在当前注解的 {@code useKeyIndex()} 为 {@code true} 时生效。
     * 它与 {@code key()} 参数互斥。
     *
     * @see #useKeyIndex()
     */
    int index() default -1;
}
