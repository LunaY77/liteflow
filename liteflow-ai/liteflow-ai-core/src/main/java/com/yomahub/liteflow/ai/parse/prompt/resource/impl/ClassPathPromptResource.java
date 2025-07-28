package com.yomahub.liteflow.ai.parse.prompt.resource.impl;

import com.yomahub.liteflow.ai.domain.enums.ResourcePrefixEnum;
import com.yomahub.liteflow.ai.parse.prompt.resource.AbstractPromptResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 类路径
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassPathPromptResource extends AbstractPromptResource {

    private final String path;
    private final ClassLoader classLoader;

    public ClassPathPromptResource(String path) {
        this(path, null);
    }

    public ClassPathPromptResource(String path, ClassLoader classLoader) {
        this.path = subPrefix(path);
        this.classLoader = Objects.nonNull(classLoader) ? classLoader : Thread.currentThread().getContextClassLoader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        if (Objects.isNull(inputStream)) {
            throw new IOException("Resource not found: " + path);
        }
        return inputStream;
    }

    @Override
    public boolean exists() {
        return Objects.nonNull(classLoader.getResource(path));
    }

    @Override
    public String getDescription() {
        return "class path resource [" + path + "]";
    }

    @Override
    public String getURI() {
        return getResourcePrefix() + path;
    }

    @Override
    public String getResourcePrefix() {
        return ResourcePrefixEnum.CLASSPATH_PREFIX.getPrefix();
    }
}
