package com.raihert.it.services;

import com.raihert.it.exceptions.JsonParsedException;
import com.raihert.it.models.HistoricalPrice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApplicationServiceTest extends ApplicationServiceTestHelper {
    private ApplicationTestService service = new ApplicationTestService();

    @Test
    public void success() throws Exception {
        mockService(service, 200, "/historical-response.json");

        HistoricalPrice prices = service.get("url1", HistoricalPrice.class);

        assertEquals(31, prices.getBpi().size());
    }

    @Test
    public void notFound() throws Exception {
        mockService(service, 404, "some error");

        try {
            service.get("http://url1/", HistoricalPrice.class);
        } catch (final JsonParsedException ex) {
            assertEquals("some error", ex.getMessage());
        }
    }
}

class ApplicationTestService extends ApplicationService {
    // nothing
}
