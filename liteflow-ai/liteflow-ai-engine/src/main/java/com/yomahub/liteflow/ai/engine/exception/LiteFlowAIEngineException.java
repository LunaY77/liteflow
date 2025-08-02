package com.yomahub.liteflow.ai.engine.exception;

/**
 * 大模型异常
 *
 * @author 苍镜月
 * @since TODO
 */

public class LiteFlowAIEngineException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常信息
     */
    private String message;

    public LiteFlowAIEngineException(String message) {
        this.message = message;
    }

    public LiteFlowAIEngineException(String message, Throwable cause) {
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
