package com.raihert.it.services.bpi;

import com.raihert.it.exceptions.JsonParsedException;
import com.raihert.it.exceptions.ResultValidateException;
import com.raihert.it.models.CurrentPrice;
import com.raihert.it.models.HistoricalPrice;
import com.raihert.it.models.Result;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
public class BpiService {
    private Logger logger = LoggerFactory.getLogger(BpiService.class);

    @Autowired
    private CoindeskService coindeskService;

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    private final Executor executor = Executors.newFixedThreadPool(4);

    public Result handle(final String currency) throws ResultValidateException {
        final Result result = new Result(currency);

        try {
            CompletableFuture<CurrentPrice> currentPriceFuture = supplyAsync(() ->
                    coindeskService.handleCurrent(currency), executor).whenCompleteAsync((currentPrice, e) -> {
                        if (e != null) {
                            throw new CompletionException(e);
                        }

                        result.setRate(currentPrice.getBpiBy(currency).getRate());
                    }
            );

            CompletableFuture<HistoricalPrice> historicalPricesFuture = supplyAsync(() ->
                    coindeskService.handleHistorical(), executor);

            CompletableFuture.allOf(currentPriceFuture, historicalPricesFuture).get();

            if (!currentPriceFuture.isCompletedExceptionally()) {
                prepared(historicalPricesFuture.get(), currency, result);
            }

            if (!result.validate()) {
                throw new ResultValidateException(String.format("%s not valid", result));
            }
        } catch (final InterruptedException ex) {
            logger.error("facade thread error", ex);
        } catch (final ExecutionException ex) {
            logger.error("facade thread error", ex);

            if (ex.getCause() instanceof JsonParsedException) {
                throw (JsonParsedException) ex.getCause();
            }
        }

        return result;
    }

    protected void prepared(final HistoricalPrice prices, final String currency, final Result result) {
        if (prices.getBpi() == null) {
            return;
        }

        Set<Map.Entry<String, Double>> entries = prices.getBpi().entrySet();

        Asserts.check(!entries.isEmpty(), "BPI empty");

        entries.remove(entries.iterator().next());

        Asserts.check(entries.size() == 30, "Should be BTC rate in the last 30 days");

        final Map.Entry<String, Double> min = Collections.min(entries, Comparator.comparing(Map.Entry::getValue));
        final Map.Entry<String, Double> max = Collections.min(entries, Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

        try {
            CompletableFuture.allOf(supplyAsync(() ->
                    exchangeRatesService.handle(min.getKey(), currency), executor).whenCompleteAsync((rate, e) ->
                    result.setMinRate(min.getValue() / rate.getUsdRate())
            ), supplyAsync(() ->
                    exchangeRatesService.handle(max.getKey(), currency), executor).whenCompleteAsync((rate, e) ->
                    result.setMaxRate(max.getValue() / rate.getUsdRate())
            )).get();
        } catch (final InterruptedException | ExecutionException ex) {
            logger.error("facade prepared thread error", ex);
        }
    }

    protected void setCoindeskService(final CoindeskService coindeskService) {
        this.coindeskService = coindeskService;
    }

    protected void setExchangeRatesService(final ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }
}
