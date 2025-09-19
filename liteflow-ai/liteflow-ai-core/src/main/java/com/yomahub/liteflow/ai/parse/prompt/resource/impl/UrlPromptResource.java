package com.yomahub.liteflow.ai.parse.prompt.resource.impl;

import com.yomahub.liteflow.ai.domain.enums.ResourcePrefixEnum;
import com.yomahub.liteflow.ai.parse.prompt.resource.AbstractPromptResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * URL
 *
 * @author 苍镜月
 * @since TODO
 */

public class UrlPromptResource extends AbstractPromptResource {

    private static final Integer CONNECT_TIMEOUT = 10_000;
    private static final Integer READ_TIMEOUT = 30_000;
    private static final Integer CHECK_TIMEOUT = 5_000;

    private final URI uri;

    public UrlPromptResource(String url) throws URISyntaxException {
        this.uri = new URI(subPrefix(url));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URL url = uri.toURL();
        URLConnection connection = url.openConnection();

        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP request failed with response code: " + responseCode);
            }
        }

        return connection.getInputStream();
    }

    @Override
    public boolean exists() {
        try {
            URL url = uri.toURL();
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(CHECK_TIMEOUT);
            connection.setReadTimeout(CHECK_TIMEOUT);

            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            }

            try (InputStream inputStream = connection.getInputStream()) {
                return inputStream != null;
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "URL [" + uri + "]";
    }

    @Override
    public String getURI() {
        return getResourcePrefix() + uri.toString();
    }

    @Override
    public String getResourcePrefix() {
        return ResourcePrefixEnum.URL_PREFIX.getPrefix();
    }
}
