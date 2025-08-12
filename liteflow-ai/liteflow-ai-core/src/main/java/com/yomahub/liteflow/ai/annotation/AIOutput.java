package com.yomahub.liteflow.ai.annotation;

import com.yomahub.liteflow.ai.engine.model.output.ResponseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 输出节点注解
 *
 * @author 苍镜月
 * @since TODO
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIOutput {

    /**
     * 结构化输出
     * <p>
     * 默认值为 {@link ResponseType#TEXT}，表示输出为文本。
     * <p>
     * 若需要输出 JSON 格式的数据，请将此值设置为 {@link ResponseType#JSON}。
     */
    ResponseType responseType() default ResponseType.TEXT;

    /**
     * 输出数据的映射表达式（必需）。
     * <p>
     * 用于将组件的输出结果映射到 LiteFlow 上下文中。
     * 表达式的值应为上下文中目标对象的 <b>set 方法名</b>。
     * <ul>
     * <li>
     * <b>例如:</b><br>
     * 要将输出数据设置到 {@code DefaultContext} 的内部 map 中，表达式应为 {@code "setData"}。
     * 这会最终调用 {@link com.yomahub.liteflow.slot.DefaultContext#setData(String, Object)} 方法。
     * </li>
     * <li>
     * <b>例如:</b><br>
     * 要将输出数据设置到 自定义 Context 的 String 对象 {@code productName} 中，表达式应为 {@code "setProductName"}。
     * 这会最终调用 自定义上下文的 {@code setProductName(String)} 方法。
     * </li>
     * </ul>
     */
    String methodExpress() default "setData";

    /**
     * 是否启用“按键名/索引”的映射策略（默认为 false）。
     * <p>
     * 当为 {@code true} 时，框架将使用 {@code key()} 或 {@code index()} 的值，
     * 配合 {@link AIOutput#methodExpress()} 来定位上下文中的目标位置。
     * <p>
     * 当为 {@code false} 时，{@code key()} 和 {@code index()} 的值将被忽略。
     * 直接使用 {@link AIOutput#methodExpress()} 指定的方法名来设置数据。
     */
    boolean useKeyIndex() default false;

    /**
     * 如需启用，请开启 {@link AIOutput#useKeyIndex()}
     * <p>
     * 键名（非必填），用于向 Map 类型的目标输出数据。
     * <p>
     * 当输出目标是 {@code Map} 或其他键值对结构时，此参数用于指定存入数据时所使用的键。
     * 默认值为空字符串
     * <ul>
     * <li>
     * <b>例如:</b><br>
     * 当使用 {@code "setData"} 表达式映射到 {@code DefaultContext} 的 dataMap 时，
     * 若指定 {@code key = "myResult"}，则输出数据会以 "myResult" 为键存入 Map 中。
     * </li>
     * </ul>
     * <b>注意：</b>此参数与 {@code index()} 参数互斥，不应同时设置。
     */
    String key() default "";

    /**
     * 如需启用，请开启 {@link AIOutput#useKeyIndex()}
     * <p>
     * 索引（非必填），用于向 List 或数组类型的目标输出数据。
     * <p>
     * 当输出目标是 {@code List}、数组或其它按索引访问的集合时，此参数用于指定存入数据时的位置。
     * 它的默认值为 {@code -1}，这个特殊值表示用户未设置此索引。
     * <ul>
     * <li>
     * <b>例如:</b><br>
     * 若输出目标是上下文中的一个 List 对象，指定 {@code index = 0} 会将数据设置或替换到 List 的第一个位置。
     * </li>
     * </ul>
     * <b>注意：</b>此参数与 {@code key()} 参数互斥，不应同时设置。
     */
    int index() default -1;

    /**
     * Json 输出的目标类名，格式为 {@code com.example.MyEntity} 或 {@code java.util.List<com.example.MyEntity>} 或 {@code java.util.Map<String, com.example.MyEntity>}
     * <p>
     * 如需启用，请设置 {@link AIOutput#responseType()} 为 {@link ResponseType#JSON}
     * <p>
     * 表示输出的 JSON Schema 定义。如果需要添加描述信息，请使用{@link com.yomahub.liteflow.ai.engine.model.output.structure.Description}
     */
    String typeName() default "java.lang.String";

    /**
     * 是否严格模式（默认为 true，输出模式为 JSON 时使用）
     */
    boolean strict() default true;

    /**
     * 如需启用，请设置 {@link AIOutput#responseType()} 为 {@link ResponseType#JSON}
     * <p>
     * 输出字段映射配置
     * <p>
     * 用于将结构化输出对象 ({@code entityClass}) 中的某一个字段，
     * 精确映射到 LiteFlow 上下文的指定位置。
     */
    OutputField[] mapping() default {};
}
