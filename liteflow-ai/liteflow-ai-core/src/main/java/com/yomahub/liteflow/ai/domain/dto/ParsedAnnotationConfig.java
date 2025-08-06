package com.yomahub.liteflow.ai.domain.dto;

import com.yomahub.liteflow.ai.engine.model.output.ResponseType;

/**
 * 解析后的注解配置类
 *
 * @author 苍镜月
 * @since TODO
 */

public class ParsedAnnotationConfig {

    private String systemPrompt;
    private String userPrompt;
    private ResponseType responseType = ResponseType.TEXT;
    private Class<?> entityClass = String.class;

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }
}
