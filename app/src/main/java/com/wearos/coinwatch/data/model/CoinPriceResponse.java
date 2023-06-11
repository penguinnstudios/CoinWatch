package com.wearos.coinwatch.data.model;

import com.google.gson.annotations.SerializedName;

public class CoinPriceResponse {

    @SerializedName("id")
    private String coinQueryId;

    @SerializedName("current_price")
    private double price;

    @SerializedName("price_change_percentage_24h")
    private double percentChange;

    public String getCoinQueryId() {
        return coinQueryId;
    }

    public double getPrice() {
        return price;
    }

    public double getPercentChange() {
        return percentChange;
    }
}
