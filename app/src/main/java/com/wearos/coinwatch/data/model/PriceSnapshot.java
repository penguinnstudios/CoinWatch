package com.wearos.coinwatch.data.model;

import com.wearos.coinwatch.util.FormatUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PriceSnapshot {

    private final double price;
    private final String formattedDate;
    private final String formattedPrice;
    private static final String HOURLY = "h:mm a";
    private static final String DAY_OF_MONTH = "d MMM";

    public PriceSnapshot(double timestamp, double price, Currency currency, MarketChartType marketChartType) {
        this.price = price;

        switch (marketChartType) {
            case DAY:
                SimpleDateFormat dateFormatHour = new SimpleDateFormat(HOURLY, Locale.US);
                this.formattedDate = dateFormatHour.format(timestamp);
                break;

            case WEEK:

            case MONTH:

            case YEAR:
                SimpleDateFormat dateFormatDay = new SimpleDateFormat(DAY_OF_MONTH, Locale.US);
                this.formattedDate = dateFormatDay.format(timestamp);
                break;

            default:
                throw new IllegalStateException("Invalid market interval");
        }

        this.formattedPrice = FormatUtils.formatPrice(currency, price);
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public double getPrice() {
        return price;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
}
