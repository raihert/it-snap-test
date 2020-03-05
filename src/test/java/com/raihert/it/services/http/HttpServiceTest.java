package com.raihert.it.services.http;

import com.raihert.it.ApplicationTest;
import com.raihert.it.exceptions.JsonParsedException;
import com.raihert.it.models.HistoricalPrice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpServiceTest extends ApplicationTest {
    private final HttpServiceImpl service = new HttpServiceImpl();

    @Test
    public void success() throws Exception {
        service.setHttpclient(mockService(200, "/historical-response.json"));

        HistoricalPrice prices = service.get("url1", HistoricalPrice.class);

        assertEquals(31, prices.getBpi().size());
    }

    @Test
    public void notFound() throws Exception {
        service.setHttpclient(mockService(404, "some error"));

        try {
            service.get("http://url1/", HistoricalPrice.class);
        } catch (final JsonParsedException ex) {
            assertEquals("some error", ex.getMessage());
        }
    }
}

class HttpServiceImpl extends HttpService {
    // nothing
}
