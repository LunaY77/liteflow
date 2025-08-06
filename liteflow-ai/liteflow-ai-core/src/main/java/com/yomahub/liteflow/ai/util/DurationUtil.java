package com.yomahub.liteflow.ai.util;

import cn.hutool.core.util.StrUtil;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将时间字符串转换为 {@link java.time.Duration} 工具类
 *
 * @author 苍镜月
 * @since TODO
 */

public class DurationUtil {
    /**
     * 用于解析简化时间格式 (e.g., "10s", "5m", "1h") 的正则表达式.
     * 它捕获一个或多个数字作为第一组，以及单位 's', 'm', 或 'h' 作为第二组.
     */
    private static final Pattern SIMPLE_FORMAT_PATTERN = Pattern.compile("(\\d+)([smh])", Pattern.CASE_INSENSITIVE);

    /**
     * 私有构造函数
     */
    private DurationUtil() {
    }

    /**
     * <p>将一个字符串解析为 {@link Duration} 对象。</p>
     *
     * <p>该方法提供了两种格式的解析支持：</p>
     * <ol>
     * <li><b>标准 ISO 8601 持续时间格式:</b> 这是 Java 内置支持的标准格式，以 {@code "PT"} 开头。
     * 这是首选的解析方式。</li>
     * <li><b>自定义简化格式:</b> 为了方便用户配置，支持一种更简洁的格式，由数字和单位后缀组成。
     * 支持的单位包括：
     * <ul>
     * <li>{@code s} - 秒</li>
     * <li>{@code m} - 分钟</li>
     * <li>{@code h} - 小时</li>
     * </ul>
     * 单位不区分大小写 (例如, {@code "10S"} 和 {@code "10s"} 效果相同)。
     * </li>
     * </ol>
     *
     * <p><b>解析逻辑:</b></p>
     * <p>方法会首先尝试使用标准的 ISO 8601 格式进行解析。如果解析失败，它会回退 (fallback) 到自定义的
     * 简化格式进行尝试。如果两种格式都无法解析，或者输入字符串为 {@code null} 或空，
     * 将返回指定的默认 {@code Duration} 对象。</p>
     *
     * @param durationStr     要解析的持续时间字符串。
     *                        <p><b>ISO 8601 格式示例:</b></p>
     *                        <ul>
     *                        <li>{@code "PT20S"} - 20 秒</li>
     *                        <li>{@code "PT15M"} - 15 分钟</li>
     *                        <li>{@code "PT10H"} - 10 小时</li>
     *                        <li>{@code "P2D"} - 2 天</li>
     *                        </ul>
     *                        <p><b>自定义简化格式示例:</b></p>
     *                        <ul>
     *                        <li>{@code "30s"} - 30 秒</li>
     *                        <li>{@code "5m"} - 5 分钟</li>
     *                        <li>{@code "1h"} - 1 小时</li>
     *                        </ul>
     * @param defaultDuration 当输入字符串无法被解析、为 {@code null} 或为空时，返回的默认 {@code Duration} 对象。
     *                        此参数不能为 {@code null}。
     * @return 解析后的 {@link Duration} 对象，或在无法解析时返回 {@code defaultDuration}。
     * @throws NullPointerException 如果 {@code defaultDuration} 为 {@code null}。
     * @see java.time.Duration#parse(CharSequence)
     */
    public static Duration toDuration(String durationStr, Duration defaultDuration) {
        if (StrUtil.isBlank(durationStr)) {
            return defaultDuration;
        }

        // 1. 尝试使用标准的 ISO 8601 格式进行解析
        try {
            return Duration.parse(durationStr);
        } catch (DateTimeParseException ignore) {
        }

        // 2. 尝试使用自定义的简化格式进行解析
        Matcher matcher = SIMPLE_FORMAT_PATTERN.matcher(durationStr.trim());

        if (matcher.matches()) {
            try {
                long value = Long.parseLong(matcher.group(1));
                String unit = matcher.group(2).toLowerCase();

                switch (unit) {
                    case "s":
                        return Duration.ofSeconds(value);
                    case "m":
                        return Duration.ofMinutes(value);
                    case "h":
                        return Duration.ofHours(value);
                    default:
                        break;
                }
            } catch (Exception ignore) {
            }
        }
        // 3. 如果两种格式都无法解析，返回默认的 Duration
        return defaultDuration;
    }
}
