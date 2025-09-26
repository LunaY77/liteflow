package com.yomahub.liteflow.ai.parse.prompt.resource.impl;

import com.yomahub.liteflow.ai.domain.enums.ResourcePrefixEnum;
import com.yomahub.liteflow.ai.parse.prompt.resource.AbstractPromptResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class FileSystemPromptResource extends AbstractPromptResource {

    private final Path filePath;

    public FileSystemPromptResource(String path) {
        this.filePath = Paths.get(subPrefix(path));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!exists()) {
            throw new IOException("File does not exist or is not readable: " + filePath);
        }
        return Files.newInputStream(filePath);
    }

    @Override
    public boolean exists() {
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    @Override
    public String getDescription() {
        return "file [" + filePath.toAbsolutePath() + "]";

    }

    @Override
    public String getURI() {
        return getResourcePrefix() + filePath.toAbsolutePath();
    }

    @Override
    public String getResourcePrefix() {
        return ResourcePrefixEnum.FILE_PREFIX.getPrefix();
    }
}
