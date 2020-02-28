package com.raihert.it.models;

public class Result {
    private String currency;

    // The current Bitcoin rate, in the requested currency
    private Double rate;

    // The lowest Bitcoin rate in the last 30 days, in the requested currency
    private Double minRate;

    // The highest Bitcoin rate in the last 30 days, in the requested currency
    private Double maxRate;

    public Result(final String currency) {
        this.currency = currency;
    }

    public boolean validate() {
        if (currency == null || currency.isEmpty()) {
            return false;
        }

        if (rate == null) {
            return false;
        }

        if (minRate != null && maxRate != null) {
            if (maxRate < minRate) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(final Double rate) {
        this.rate = rate;
    }

    public Double getMinRate() {
        return minRate;
    }

    public void setMinRate(final Double minRate) {
        this.minRate = Double.parseDouble(String.format("%.4f", minRate));
    }

    public Double getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(final Double maxRate) {
        this.maxRate = Double.parseDouble(String.format("%.4f", maxRate));
    }

    @Override
    public String toString() {
        return "Result{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                ", minRate=" + minRate +
                ", maxRate=" + maxRate +
                '}';
    }
}
