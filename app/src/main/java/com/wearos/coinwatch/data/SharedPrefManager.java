package com.wearos.coinwatch.data;

import android.content.SharedPreferences;

import com.wearos.coinwatch.data.model.Currency;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPrefManager {

    private static final String FIRST_TIME_OPENING_APP = "FIRST_TIME_OPENING_APP";
    private static final String CURRENCY_TYPE = "CURRENCY_TYPE";
    private static final String SELECTED_COIN_QUERY_ID = "SELECTED_COIN_QUERY_ID";
    private static final String COIN_PRICE = "COIN_PRICE";
    private static final String PERCENT_CHANGE = "PERCENT_CHANGE";
    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPrefManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isFirstTimeOpeningApp() {
        return sharedPreferences.getBoolean(FIRST_TIME_OPENING_APP, true);
    }

    public void setFirstTimeOpeningApp(boolean b) {
        sharedPreferences.edit().putBoolean(FIRST_TIME_OPENING_APP, b).apply();
    }

    public Currency getCurrencyType() {
        String currency = sharedPreferences.getString(CURRENCY_TYPE, Currency.USD.getCurrencyQueryId());
        return Currency.of(currency).orElseThrow(() ->
                new NullPointerException("Currency does not exist: " + currency));
    }

    public void setCurrencyType(String currencyType) {
        sharedPreferences.edit().putString(CURRENCY_TYPE, currencyType).apply();
    }

    public void setSelectedCoin(String coinQueryId) {
        sharedPreferences.edit().putString(SELECTED_COIN_QUERY_ID, coinQueryId).apply();
    }

    public String getSelectedCoin() {
        return sharedPreferences.getString(SELECTED_COIN_QUERY_ID, null);
    }

    public void setCoinPrice(String coinPrice) {
        sharedPreferences.edit().putString(COIN_PRICE, coinPrice).apply();
    }

    public String getCoinPrice() {
        return sharedPreferences.getString(COIN_PRICE, null);
    }

    public void setPercentChange(String coinPercentage) {
        sharedPreferences.edit().putString(PERCENT_CHANGE, coinPercentage).apply();
    }

    public String getPercentChange() {
        return sharedPreferences.getString(PERCENT_CHANGE, null);
    }
}
