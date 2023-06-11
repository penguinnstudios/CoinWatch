package com.wearos.coinwatch.data.uistate;

public class MainUIState {

    public enum State {
        CREATE_FAVORITE_COIN_PLACEHOLDERS,
        SUCCESS_GET_PRICES,
        FAILED_GET_PRICES,
        UNEXPECTED_ERROR;
    }

    private final State state;
    private String errMsg;

    private MainUIState(State state) {
        this.state = state;
    }

    private MainUIState(State state, String errMsg) {
        this.state = state;
        this.errMsg = errMsg;
    }

    public static MainUIState successGetCoinPrices() {
        return new MainUIState(State.SUCCESS_GET_PRICES);
    }

    public static MainUIState successCreateFavoriteCoinPlaceholders() {
        return new MainUIState(State.CREATE_FAVORITE_COIN_PLACEHOLDERS);
    }

    public static MainUIState failed(String errMsg) {
        return new MainUIState(State.FAILED_GET_PRICES, errMsg);
    }

    public static MainUIState unexpectedError(){
        return new MainUIState(State.UNEXPECTED_ERROR);
    }

    public State getType() {
        return state;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
