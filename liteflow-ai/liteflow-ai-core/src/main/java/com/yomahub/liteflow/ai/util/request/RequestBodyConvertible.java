package com.yomahub.liteflow.ai.util.request;

/**
 * 实现该接口的类可以转换为请求体。
 *
 * @author 苍镜月
 * @since TODO
 */

public interface RequestBodyConvertible {

    /**
     * 将当前对象转换为 {@link RequestBody} 实例。
     *
     * @return 转换后的 {@link RequestBody} 实例
     */
    RequestBody toRequestBody();
}
