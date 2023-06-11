package com.wearos.coinwatch.data.uistate;

public class SettingsUIState {

    public enum State {
        SUCCESS_UPDATE_CURRENCY,
        SUCCESS_GET_CURRENCY
    }

    private final State state;
    private final String currencyName;

    private SettingsUIState(State state, String currencyName) {
        this.state = state;
        this.currencyName = currencyName;
    }

    public static SettingsUIState successUpdateCurrency(String currencyName) {
        return new SettingsUIState(State.SUCCESS_UPDATE_CURRENCY, currencyName);
    }

    public static SettingsUIState successGetCurrency(String currencyName) {
        return new SettingsUIState(State.SUCCESS_GET_CURRENCY, currencyName);
    }

    public State getState() {
        return state;
    }

    public String getCurrencyName() {
        return currencyName;
    }
}
