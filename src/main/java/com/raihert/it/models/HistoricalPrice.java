package com.raihert.it.models;

import java.util.LinkedHashMap;

//response:
//{
//    "bpi": {
//        "2020-02-25": 9313.92,
//        "2020-02-26": 8790.12
//    },
//    "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index. BPI value data returned as USD.",
//    "time": {
//        "updated": "Feb 27, 2020 00:03:00 UTC",
//        "updatedISO": "2020-02-27T00:03:00+00:00"
//    }
//}
public class HistoricalPrice {
    private LinkedHashMap<String, Double> bpi;

    public LinkedHashMap<String, Double> getBpi() {
        return bpi;
    }

    public void setBpi(final LinkedHashMap<String, Double> bpi) {
        this.bpi = bpi;
    }

    @Override
    public String toString() {
        return "HistoricalPrice{" +
                "bpi=" + bpi +
                '}';
    }
}
