package com.raihert.it;

import com.raihert.it.exceptions.ResultValidateException;
import com.raihert.it.models.CurrentPrice;
import com.raihert.it.models.ExchangeRate;
import com.raihert.it.models.HistoricalPrice;
import com.raihert.it.models.Result;
import com.raihert.it.services.ApplicationServiceTestHelper;
import com.raihert.it.services.CoindeskService;
import com.raihert.it.services.ExchangeRatesService;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationFacadeTest extends ApplicationServiceTestHelper {
    private ApplicationFacade facade = new ApplicationFacade();

    @Test
    public void handleSuccess() throws Exception {
        CoindeskService coindeskService = mock(CoindeskService.class);
        when(coindeskService.handleCurrent("RUR")).thenReturn(new CurrentPrice() {{
            setBpi(new HashMap<String, BPI>() {{
                put("RUR", new BPI() {{
                    setRate_float(111.0);
                }});
                put("USD", new BPI() {{
                    setRate_float(-1.0);
                }});
            }});
        }});
        when(coindeskService.handleHistorical()).thenReturn(new HistoricalPrice() {{
            setBpi(new LinkedHashMap<String, Double>() {{
                for (int i = 1; i <= 31; i++) {
                    put(String.valueOf(i), 1.0 * i);
                }
            }});
        }});
        facade.setCoindeskService(coindeskService);

        ExchangeRatesService exchangeRatesService = mock(ExchangeRatesService.class);
        when(exchangeRatesService.handle("2", "RUR")).thenReturn(new ExchangeRate() {{
            setRates(new HashMap<String, Double>() {{
                put("USD", 1.0);
            }});
        }});
        when(exchangeRatesService.handle("31", "RUR")).thenReturn(new ExchangeRate() {{
            setRates(new HashMap<String, Double>() {{
                put("USD", 0.1);
            }});
        }});

        facade.setExchangeRatesService(exchangeRatesService);

        Result result = facade.handle("RUR");

        assertEquals("RUR", result.getCurrency());
        assertEquals(111.0, result.getRate(), 0);
        assertEquals(2.0, result.getMinRate(), 0);
        assertEquals(310.0, result.getMaxRate(), 0);
    }

    @Test
    public void handleValidationFailed() throws Exception {
        CoindeskService coindeskService = mock(CoindeskService.class);
        when(coindeskService.handleCurrent("RUR")).thenReturn(new CurrentPrice() {{
            setBpi(new HashMap<String, BPI>() {{
                put("RUR", new BPI() {{
                    setRate_float(null);
                }});
                put("USD", new BPI() {{
                    setRate_float(-1.0);
                }});
            }});
        }});
        when(coindeskService.handleHistorical()).thenReturn(new HistoricalPrice() {{
            setBpi(new LinkedHashMap<String, Double>() {{
                for (int i = 31; i >= 1; i--) {
                    put(String.valueOf(i), 1.0 * i);
                }
            }});
        }});
        facade.setCoindeskService(coindeskService);

        ExchangeRatesService exchangeRatesService = mock(ExchangeRatesService.class);
        when(exchangeRatesService.handle(anyString(), eq("RUR"))).thenReturn(new ExchangeRate() {{
            setRates(new HashMap<String, Double>() {{
                put("USD", 1.0);
            }});
        }});

        facade.setExchangeRatesService(exchangeRatesService);

        try {
            facade.handle("RUR");
        } catch (final ResultValidateException ex) {
            assertEquals("Result{currency='RUR', rate=null, minRate=1.0, maxRate=30.0} not valid", ex.getMessage());
        }
    }

    @Test
    public void successPrepared() throws Exception {
        final Result result = new Result("RUR");

        HistoricalPrice prices = new HistoricalPrice() {{
            setBpi(new LinkedHashMap<String, Double>() {{
                for (int i = 1; i <= 31; i++) {
                    put(String.valueOf(i), 1.0 * i);
                }
            }});
        }};

        ExchangeRatesService exchangeRatesService = mock(ExchangeRatesService.class);
        when(exchangeRatesService.handle("2", "RUR")).thenReturn(new ExchangeRate() {{
            setRates(new HashMap<String, Double>() {{
                put("USD", 2.0);
            }});
        }});
        when(exchangeRatesService.handle("31", "RUR")).thenReturn(new ExchangeRate() {{
            setRates(new HashMap<String, Double>() {{
                put("USD", 0.5);
            }});
        }});

        facade.setExchangeRatesService(exchangeRatesService);

        facade.prepared(prices, "RUR", result);

        assertEquals("RUR", result.getCurrency());
        assertEquals(null, result.getRate());
        assertEquals(1.0, result.getMinRate(), 0);
        assertEquals(62.0, result.getMaxRate(), 0);
    }

    @Test
    public void wrongResponsePrepared() throws Exception {
        Result result = new Result("RUR");

        try {
            facade.prepared(new HistoricalPrice(), "RUR", result);
        } catch (final IllegalStateException ex) {
            assertEquals("BPI empty", ex.getMessage());
        }

        HistoricalPrice prices = new HistoricalPrice() {{
            setBpi(new LinkedHashMap<String, Double>() {{
                for (int i = 1; i <= 30; i++) {
                    put(String.valueOf(i), 1.0 * i);
                }
            }});
        }};

        try {
            facade.prepared(prices, "RUR", result);
        } catch (final IllegalStateException ex) {
            assertEquals("Should be BTC rate in the last 30 days", ex.getMessage());
        }
    }
}