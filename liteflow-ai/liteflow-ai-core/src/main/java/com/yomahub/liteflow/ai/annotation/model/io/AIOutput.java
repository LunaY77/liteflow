package com.yomahub.liteflow.ai.annotation.model.io;

import com.yomahub.liteflow.ai.engine.model.output.ResponseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 输出节点注解
 *
 * @author 苍镜月
 * @since 2.16.0
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
     * 输出数据的映射方法表达式 (必需)。
     * <p>
     * 用于将组件的输出结果通过表达式设置到 LiteFlow 的上下文中。
     *
     * <p>
     * <b>占位符使用约定:</b>
     * <ul>
     * <li>表达式中必须包含<b>且仅包含一个</b>用于AI输出结果的占位符。</li>
     * <li>占位符必须是一个以 {@code $} 符号开头的变量名 (例如: {@code $output}, {@code $result})。</li>
     * <li>在运行时，框架会将此占位符替换为实际的AI输出结果。</li>
     * </ul>
     *
     * <b>关于字符串字面量与转义字符的说明:</b>
     * <p>
     * 当你的表达式需要包含一个字符串字面量参数时（例如Map的key），你必须用双引号将其包裹。
     * 由于 {@code methodExpress} 本身就是一个Java字符串，因此内部的双引号必须使用反斜杠 ({@code \}) 进行转义。
     * 这是Java语言的标准要求。
     * <p>
     * <b>例如:</b> 如果你想调用 {@code setMap("myKey", <AI输出结果>)}，那么表达式必须写成:
     * {@code "setMap(\"myKey\", $output)"}
     *
     * <p>
     * <b>更多示例:</b>
     * <ul>
     * <li><b>简单设置:</b> {@code "setProductName($output)"}<br>
     * 最终执行: {@code context.setProductName("AI的输出结果")}
     * </li>
     * <li><b>设置到DefaultContext的Map中:</b> {@code "setData(\"productName\", $result)"}<br>
     * 最终执行: {@code defaultContext.setData("productName", "AI的输出结果")}
     * </li>
     * <li><b>通过索引设置到List或数组中:</b> {@code "setProducts(0, $product)"}<br>
     * 最终执行: {@code context.setProducts(0, "AI的输出结果")}
     * </li>
     * </ul>
     */
    String methodExpress() default "setData(\"result\", $output)";

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
