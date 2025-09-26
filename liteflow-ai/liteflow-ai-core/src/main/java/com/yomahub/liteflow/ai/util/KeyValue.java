package com.yomahub.liteflow.ai.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注解用于标记键值对
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface KeyValue {
    String key();
    String value();
}
