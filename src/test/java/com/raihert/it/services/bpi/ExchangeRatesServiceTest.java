package com.raihert.it.services.bpi;

import com.raihert.it.ApplicationTest;
import com.raihert.it.models.ExchangeRate;
import com.raihert.it.services.http.HttpService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExchangeRatesServiceTest extends ApplicationTest {
    private final ExchangeRatesService service = new ExchangeRatesService() {{
        setUrl("https://api.exchangeratesapi.io/%s?base=%s&symbols=USD");
    }};

    @Test
    public void success() throws Exception {
        service.setHelper(new HttpService(mockService(200, "/rate-response.json")));

        assertEquals("https://api.exchangeratesapi.io/2000-01-01?base=RUR&symbols=USD", service.url("2000-01-01", "RUR"));

        ExchangeRate rate = service.handle("2000-01-01", "RUR");

        assertEquals(0.9680459924, rate.getUsdRate(), 0);
    }
}