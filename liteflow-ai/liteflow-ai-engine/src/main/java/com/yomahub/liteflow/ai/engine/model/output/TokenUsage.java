package com.yomahub.liteflow.ai.engine.model.output;

import java.util.Objects;

/**
 * Token使用
 *
 * @author 苍镜月
 * @since TODO
 */

public class TokenUsage {

    private final Integer inputTokenCount;
    private final Integer outputTokenCount;
    private final Integer totalTokenCount;

    public TokenUsage() {
        this(null);
    }

    public TokenUsage(Integer inputTokenCount) {
        this(inputTokenCount, null);
    }

    public TokenUsage(Integer inputTokenCount, Integer outputTokenCount) {
        this(inputTokenCount, outputTokenCount, sum(inputTokenCount, outputTokenCount));
    }

    public TokenUsage(Integer inputTokenCount, Integer outputTokenCount, Integer totalTokenCount) {
        this.inputTokenCount = inputTokenCount;
        this.outputTokenCount = outputTokenCount;
        this.totalTokenCount = totalTokenCount;
    }

    public Integer getInputTokenCount() {
        return inputTokenCount;
    }

    public Integer getOutputTokenCount() {
        return outputTokenCount;
    }

    public Integer getTotalTokenCount() {
        return totalTokenCount;
    }

    /**
     * 计算两个 TokenUsage 对象的总和
     *
     * @param a 第一个 TokenUsage 对象
     * @param b 第二个 TokenUsage 对象
     * @return 一个新的 TokenUsage 对象，包含两个对象的 token 使用总和
     */
    public static TokenUsage sum(TokenUsage a, TokenUsage b) {
        if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        } else {
            return a.add(b);
        }
    }

    /**
     * 将当前 TokenUsage 与另一个 TokenUsage 相加
     *
     * @param other 另一个 TokenUsage 对象
     * @return 一个新的 TokenUsage 对象，包含两个对象的 token 使用总和
     */
    public TokenUsage add(TokenUsage other) {
        if (Objects.isNull(other)) {
            return this;
        }
        if (other.getClass() != TokenUsage.class) {
            return other.add(this);
        }
        return new TokenUsage(
                sum(this.inputTokenCount, other.inputTokenCount),
                sum(this.outputTokenCount, other.outputTokenCount),
                sum(this.totalTokenCount, other.totalTokenCount)
        );
    }

    /**
     * 计算两个整数的和
     *
     * @return 整数和
     */
    protected static Integer sum(Integer a, Integer b) {
        if (Objects.isNull(a)) {
            return b;
        } else if (Objects.isNull(b)) {
            return a;
        } else {
            return a + b;
        }
    }

    @Override
    public String toString() {
        return "TokenUsage{" +
                "inputTokenCount=" + inputTokenCount +
                ", outputTokenCount=" + outputTokenCount +
                ", totalTokenCount=" + totalTokenCount +
                '}';
    }
}
