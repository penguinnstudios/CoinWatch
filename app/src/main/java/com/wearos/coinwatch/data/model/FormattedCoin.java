package com.wearos.coinwatch.data.model;

import com.wearos.coinwatch.util.FormatUtils;
import com.wearos.coinwatch.util.MainRecyclerItem;

public class FormattedCoin implements MainRecyclerItem {

    private static final double PLACEHOLDER_PRICE = 0;
    private static final double PLACEHOLDER_PERCENT_CHANGE = 0;
    private final Coin coin;
    private final double price;
    private final double percentChange;
    private final SignedNumber signedNumber;
    private final String formattedPrice;
    private final String formattedPercentChange;

    public FormattedCoin(Coin coin, Currency currency, double price, double percentChange) {
        this.coin = coin;
        this.price = price;
        this.percentChange = percentChange;
        this.formattedPrice = FormatUtils.formatPrice(currency, price);

        SignedNumber signedNumber = FormatUtils.convertToSignedNumber(percentChange);

        this.signedNumber = signedNumber;
        this.formattedPercentChange = FormatUtils.formatPercentChange(signedNumber, percentChange);
    }

    public FormattedCoin(Coin coin, Currency currency) {
        this.coin = coin;
        this.price = PLACEHOLDER_PRICE;
        this.percentChange = PLACEHOLDER_PERCENT_CHANGE;
        this.formattedPrice = FormatUtils.formatPrice(currency, PLACEHOLDER_PRICE);

        SignedNumber signedNumber = FormatUtils.convertToSignedNumber(PLACEHOLDER_PERCENT_CHANGE);

        this.signedNumber = signedNumber;
        this.formattedPercentChange = FormatUtils.formatPercentChange(signedNumber, PLACEHOLDER_PERCENT_CHANGE);
    }

    public Coin getCoinType() {
        return coin;
    }

    public SignedNumber getSignedNumber() {
        return signedNumber;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public String getFormattedPercentChange() {
        return formattedPercentChange;
    }

    public double getPrice() {
        return price;
    }

    public double getPercentChange() {
        return percentChange;
    }
}
