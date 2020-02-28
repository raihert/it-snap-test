package com.raihert.it.models;

import java.util.Map;

//response:
//{"rates":{"USD":0.9680459924},"base":"CAD","date":"2010-01-12"}
public class ExchangeRate {
    public static final String USD = "USD";

    private Map<String, Double> rates;

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(final Map<String, Double> rates) {
        this.rates = rates;
    }

    public Double getUsdRate() {
        return rates.get(USD);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "rates=" + rates +
                '}';
    }
}
