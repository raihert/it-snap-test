package com.raihert.it.services;

import com.raihert.it.models.CurrentPrice;
import com.raihert.it.models.HistoricalPrice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoindeskServiceServiceTestHelper extends ApplicationServiceTestHelper {
    private CoindeskService service = new CoindeskService();

    @Test
    public void successHandleCurrent() throws Exception {
        mockService(service, 200, "/current-response.json");

        assertEquals("https://api.coindesk.com/v1/bpi/currentprice/RUR.json", service.currentPriceUrl("RUR"));

        CurrentPrice price = service.handleCurrent("RUR");

        assertEquals("USD", price.getBpiBy("USD").getCode());
        assertEquals(8812.2417, price.getBpiBy("USD").getRate(), 0);

        assertEquals("EUR", price.getBpiBy("EUR").getCode());
        assertEquals(8078.9574, price.getBpiBy("EUR").getRate(), 0);

        assertEquals("GBP", price.getBpiBy("GBP").getCode());
        assertEquals(6815.3613, price.getBpiBy("GBP").getRate(), 0);
    }

    @Test
    public void successHandleHistorical() throws Exception {
        mockService(service, 200, "/historical-response.json");

        HistoricalPrice prices = service.handleHistorical();

        assertEquals(31, prices.getBpi().size());
        assertEquals(8901.5067, prices.getBpi().get("2020-01-27"), 0);
        assertEquals(9664.6333, prices.getBpi().get("2020-02-24"), 0);
        assertEquals(9313.92, prices.getBpi().get("2020-02-25"), 0);
        assertEquals(8790.12, prices.getBpi().get("2020-02-26"), 0);
    }
}