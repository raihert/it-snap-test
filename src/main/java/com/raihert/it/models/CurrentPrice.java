package com.raihert.it.models;

import java.util.Map;

//response:
//{
//  "time": {
//    "updated": "Feb 27, 2020 10:43:00 UTC",
//    "updatedISO": "2020-02-27T10:43:00+00:00",
//    "updateduk": "Feb 27, 2020 at 10:43 GMT"
//  },
//  "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
//  "bpi": {
//    "USD": {
//      "code": "USD",
//      "rate": "8,813.0500",
//      "description": "United States Dollar",
//      "rate_float": 8813.05
//    },
//    "CAD": {
//      "code": "CAD",
//      "rate": "11,743.8298",
//      "description": "Canadian Dollar",
//      "rate_float": 11743.8298
//    }
//  }
//}
public class CurrentPrice {
    private Map<String, BPI> bpi;

    public Map<String, BPI> getBpi() {
        return bpi;
    }

    public void setBpi(final Map<String, BPI> bpi) {
        this.bpi = bpi;
    }

    public BPI getBpiBy(final String currency) {
        return getBpi().get(currency);
    }

    @Override
    public String toString() {
        return "CurrentPrice{" +
                "bpi=" + bpi +
                '}';
    }

    public static class BPI {
        private String code;
        private Double rate_float;

        public String getCode() {
            return code;
        }

        public void setCode(final String code) {
            this.code = code;
        }

        public Double getRate() {
            return rate_float;
        }

        public void setRate_float(final Double rate_float) {
            this.rate_float = rate_float;
        }

        @Override
        public String toString() {
            return "BPI{" +
                    "code='" + code + '\'' +
                    ", rate_float=" + rate_float +
                    '}';
        }
    }
}
