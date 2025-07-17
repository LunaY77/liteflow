package com.yomahub.liteflow.ai.exception;

/**
 * 大模型异常
 *
 * @author 苍镜月
 * @since TODO
 */

public class LiteFlowAIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常信息
     */
    private String message;

    public LiteFlowAIException(String message) {
        this.message = message;
    }

    public LiteFlowAIException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
