package com.raihert.it.services;

import com.raihert.it.models.ExchangeRate;

import java.util.logging.Level;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class ExchangeRatesService extends ApplicationService {
    private final String URL = "https://api.exchangeratesapi.io/%s?base=%s&symbols=USD";

    public ExchangeRate handle(final String date, final String currency) {
        logger.log(Level.INFO, "exchange rates service:{0}", asList(date, currency));

        ExchangeRate rate = get(url(date, currency), ExchangeRate.class);

        logger.log(Level.INFO, "exchange rates service:response:{0}", rate);

        return rate;
    }

    protected String url(final String date, final String currency) {
        return format(URL, date, currency);
    }
}
