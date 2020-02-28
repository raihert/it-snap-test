package com.raihert.it.services;

import com.raihert.it.models.CurrentPrice;
import com.raihert.it.models.HistoricalPrice;

import java.util.logging.Level;

import static java.lang.String.format;

public class CoindeskService extends ApplicationService {
    private final String CURRENT_PRICE_URL = "https://api.coindesk.com/v1/bpi/currentprice/%s.json";
    private final String HISTORICAL_PRICE_URL = "https://api.coindesk.com/v1/bpi/historical/close.json";

    public CurrentPrice handleCurrent(final String currency) {
        logger.log(Level.INFO, "coindesk service:current:{0}", currency);

        CurrentPrice currentPrice = get(currentPriceUrl(currency), CurrentPrice.class);

        logger.log(Level.INFO, "coindesk service:current:response:{0}", currentPrice);

        return currentPrice;
    }

    public HistoricalPrice handleHistorical() {
        logger.log(Level.INFO, "coindesk service:historical");

        HistoricalPrice historicalPrices = get(HISTORICAL_PRICE_URL, HistoricalPrice.class);

        logger.log(Level.INFO, "coindesk service:historical:response:{0}", historicalPrices);

        return historicalPrices;
    }

    protected String currentPriceUrl(final String currency) {
        return format(CURRENT_PRICE_URL, currency);
    }
}
