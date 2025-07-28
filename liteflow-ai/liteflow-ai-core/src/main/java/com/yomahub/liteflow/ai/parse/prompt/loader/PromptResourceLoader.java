package com.yomahub.liteflow.ai.parse.prompt.loader;

import com.yomahub.liteflow.ai.parse.prompt.resource.PromptResource;

/**
 * 提示词资源加载器接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface PromptResourceLoader {

    /**
     * 加载资源
     *
     * @param location 资源位置
     * @return 资源
     */
    PromptResource getResource(String location) throws Exception;
}
