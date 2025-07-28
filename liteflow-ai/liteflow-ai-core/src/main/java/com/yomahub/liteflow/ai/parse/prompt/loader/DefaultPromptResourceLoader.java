package com.yomahub.liteflow.ai.parse.prompt.loader;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.parse.prompt.resource.PromptResource;
import com.yomahub.liteflow.ai.parse.prompt.resource.impl.ClassPathPromptResource;
import com.yomahub.liteflow.ai.parse.prompt.resource.impl.FileSystemPromptResource;
import com.yomahub.liteflow.ai.parse.prompt.resource.impl.TextPromptResource;
import com.yomahub.liteflow.ai.parse.prompt.resource.impl.UrlPromptResource;

import static com.yomahub.liteflow.ai.domain.enums.ResourcePrefixEnum.*;

/**
 * 默认提示词资源加载器
 *
 * @author 苍镜月
 * @since TODO
 */

public class DefaultPromptResourceLoader implements PromptResourceLoader {

    @Override
    public PromptResource getResource(String location) throws Exception {
        if (StrUtil.isBlank(location)) {
            return new TextPromptResource("");
        }

        // classpath
        if (location.startsWith(CLASSPATH_PREFIX.getPrefix())) {
            return new ClassPathPromptResource(location);
        }
        // file
        if (location.startsWith(FILE_PREFIX.getPrefix())) {
            return new FileSystemPromptResource(location);
        }
        // url
        if (location.startsWith(URL_PREFIX.getPrefix())) {
            return new UrlPromptResource(location);
        }
        // text
        return new TextPromptResource(location);
    }
}
