package com.raihert.it.services.bpi;

import com.raihert.it.services.http.HttpService;
import com.raihert.it.models.CurrentPrice;
import com.raihert.it.models.HistoricalPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class CoindeskService {
    private Logger logger = LoggerFactory.getLogger(CoindeskService.class);

    @Value("${service.bpi.price.current.url}")
    private String currentPriceUrl;

    @Value("${service.bpi.price.historical.url}")
    private String historicalPriceUrl;

    @Autowired
    private HttpService helper;

    public CurrentPrice handleCurrent(final String currency) {
        logger.info("coindesk service:current:{}", currency);

        CurrentPrice currentPrice = helper.get(currentPriceUrl(currency), CurrentPrice.class);

        logger.info("coindesk service:current:response:{}", currentPrice);

        return currentPrice;
    }

    public HistoricalPrice handleHistorical() {
        logger.info("coindesk service:historical");

        HistoricalPrice historicalPrices = helper.get(historicalPriceUrl, HistoricalPrice.class);

        logger.info("coindesk service:historical:response:{}", historicalPrices);

        return historicalPrices;
    }

    protected String currentPriceUrl(final String currency) {
        return format(currentPriceUrl, currency);
    }

    protected void setCurrentPriceUrl(final String currentPriceUrl) {
        this.currentPriceUrl = currentPriceUrl;
    }

    protected void setHistoricalPriceUrl(final String historicalPriceUrl) {
        this.historicalPriceUrl = historicalPriceUrl;
    }

    protected void setHelper(final HttpService helper) {
        this.helper = helper;
    }
}
