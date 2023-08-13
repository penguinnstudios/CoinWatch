package com.wearos.coinwatch.data.model;

public enum MarketChartType {

    WEEK(0, 7, "daily"),
    MONTH(1, 30, "daily"),
    THREE_MONTHS(2, 90, "daily"),
    YEAR(3, 365, "daily");

    private final int radioGroupIndex;
    private final int days;
    private final String marketInterval;

    MarketChartType(int radioGroupIndex, int days, String marketInterval) {
        this.radioGroupIndex = radioGroupIndex;
        this.days = days;
        this.marketInterval = marketInterval;
    }

    public int getRadioGroupIndex() {
        return radioGroupIndex;
    }

    public int getDays() {
        return days;
    }

    public String getMarketInterval() {
        return marketInterval;
    }
}
