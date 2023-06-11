package com.wearos.coinwatch.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketChartResponse {

    @SerializedName("prices")
    private List<List<Double>> prices;

    public List<List<Double>> getPrices() {
        return prices;
    }
}
