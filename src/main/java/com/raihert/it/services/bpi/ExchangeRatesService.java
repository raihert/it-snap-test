package com.raihert.it.services.bpi;

import com.raihert.it.models.ExchangeRate;
import com.raihert.it.services.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class ExchangeRatesService {
    private Logger logger = LoggerFactory.getLogger(ExchangeRatesService.class);

    @Value("${service.bpi.rates.exchange.url}")
    private String url;

    @Autowired
    private HttpService helper;

    public ExchangeRate handle(final String date, final String currency) {
        logger.info("exchange rates service:{}:{}", date, currency);

        ExchangeRate rate = helper.get(url(date, currency), ExchangeRate.class);

        logger.info("exchange rates service:response:{}", rate);

        return rate;
    }

    protected String url(final String date, final String currency) {
        return format(url, date, currency);
    }

    protected void setUrl(final String url) {
        this.url = url;
    }

    protected void setHelper(final HttpService helper) {
        this.helper = helper;
    }
}
