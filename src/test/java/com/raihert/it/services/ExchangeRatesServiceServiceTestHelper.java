package com.raihert.it.services;

import com.raihert.it.models.ExchangeRate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExchangeRatesServiceServiceTestHelper extends ApplicationServiceTestHelper {
    private ExchangeRatesService service = new ExchangeRatesService();

    @Test
    public void success() throws Exception {
        mockService(service, 200, "/rate-response.json");

        assertEquals("https://api.exchangeratesapi.io/2000-01-01?base=RUR&symbols=USD", service.url("2000-01-01", "RUR"));

        ExchangeRate rate = service.handle("2000-01-01", "RUR");

        assertEquals(0.9680459924, rate.getUsdRate(), 0);
    }
}