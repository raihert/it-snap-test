package com.raihert.it;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ApplicationTest {
    protected String resource(final String asset) throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(Paths.get(getClass().getResource(asset).toURI())));
    }

    protected CloseableHttpClient mockService(final int status, final String data) throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);

        CloseableHttpResponse resp = mock(CloseableHttpResponse.class);
        when(resp.getStatusLine()).thenReturn(new StatusLine() {
            @Override
            public ProtocolVersion getProtocolVersion() {
                return null;
            }

            @Override
            public int getStatusCode() {
                return status;
            }

            @Override
            public String getReasonPhrase() {
                return "reason";
            }
        });
        when(resp.getEntity()).thenReturn(new StringEntity(status == 200 ? resource(data) : data));
        when(httpClient.execute(any())).thenReturn(resp);

        return httpClient;
    }

}