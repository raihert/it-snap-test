package com.raihert.it.services.http;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class HttpService {
    protected Logger logger = LoggerFactory.getLogger(HttpService.class);

    @Value("${service.http.connection.timeout:1000}")
    private int timeout;

    @Value("${service.http.connection.per.route.max:2}")
    private int perRouteMax;

    @Value("${service.http.connection.total.max:2}")
    private int totalMax;

    private CloseableHttpClient httpclient;

    private ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public HttpService() {
        // nothing
    }

    public HttpService(final CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    @PostConstruct
    public void init() {
        final SocketConfig socketConfig = SocketConfig.
                custom().
                setSoTimeout(timeout).
                build();

        final PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager() {{
            setDefaultMaxPerRoute(perRouteMax);
            setMaxTotal(totalMax);

            setDefaultSocketConfig(socketConfig);
        }};

        httpclient = HttpClients.
                custom().
                setConnectionManager(pool).
                build();
    }

    public <T> T get(final String url, final Class<T> clazz) throws JsonParsedException {
        final T result;
        HttpGet httpGet = new HttpGet(url);

        try (final CloseableHttpResponse resp = httpclient.execute(httpGet)) {
            HttpEntity entity = resp.getEntity();

            if (resp.getStatusLine().getStatusCode() != 200) {
                throw new JsonParsedException(EntityUtils.toString(entity));
            }

            result = mapper.readValue(entity.getContent(), clazz);
        } catch (final IOException ex) {
            logger.error("get service error", ex);

            throw new JsonParsedException(ex);
        }

        return result;
    }

    public void setHttpclient(final CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }
}
