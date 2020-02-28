package com.raihert.it.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raihert.it.exceptions.JsonParsedException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ApplicationService {
    protected Logger logger = Logger.getGlobal();

    private final SocketConfig socketConfig = SocketConfig.
            custom().
            setSoTimeout(1_000).
            build();

    private final PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager() {{
        setDefaultMaxPerRoute(1);
        setMaxTotal(2);

        setDefaultSocketConfig(socketConfig);
    }};

    private CloseableHttpClient httpclient = HttpClients.
            custom().
            setConnectionManager(pool).
            build();

    private ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected <T> T get(final String url, final Class<T> clazz) throws JsonParsedException {
        final T result;
        HttpGet httpGet = new HttpGet(url);

        try (final CloseableHttpResponse resp = httpclient.execute(httpGet)) {
            HttpEntity entity = resp.getEntity();

            if (resp.getStatusLine().getStatusCode() != 200) {
                throw new JsonParsedException(EntityUtils.toString(entity));
            }

            result = mapper.readValue(entity.getContent(), clazz);
        } catch (final IOException ex) {
            logger.log(Level.SEVERE, "get service error", ex);

            throw new JsonParsedException(ex);
        }

        return result;
    }

    protected void setHttpclient(final CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }
}
