package com.yomahub.liteflow.ai.engine.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapperHolder
 *
 * @author 苍镜月
 * @since TODO
 */

public class ObjectMapperHolder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ObjectMapperHolder() {
    }

    public static ObjectMapper getInstance() {
        return MAPPER;
    }
}
