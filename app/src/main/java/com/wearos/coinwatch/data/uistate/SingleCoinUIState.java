package com.wearos.coinwatch.data.uistate;

import com.wearos.coinwatch.data.model.PriceSnapshot;
import com.wearos.coinwatch.data.model.SignedNumber;

import java.util.List;

public class SingleCoinUIState {

    public enum State {
        SUCCESS_GET_MARKET_CHART,
        PROGRESS_GET_MARKET_CHART,
        FAILED_GET_MARKET_CHART
    }

    private final State state;
    private SignedNumber signedNumber;
    private String formattedAbbreviation, coinName;
    private String formattedPrice, formattedPercentChange;
    private List<PriceSnapshot> priceSnapshots;
    private String errMsg;

    public SingleCoinUIState(State state, String formattedAbbreviation, String coinName) {
        this.state = state;
        this.formattedAbbreviation = formattedAbbreviation;
        this.coinName = coinName;
    }

    public SingleCoinUIState(
            State state,
            String formattedPrice,
            String formattedPercentChange,
            SignedNumber signedNumber,
            List<PriceSnapshot> priceSnapshots) {

        this.state = state;
        this.formattedPrice = formattedPrice;
        this.formattedPercentChange = formattedPercentChange;
        this.signedNumber = signedNumber;
        this.priceSnapshots = priceSnapshots;
    }

    public SingleCoinUIState(State state, String errMsg) {
        this.state = state;
        this.errMsg = errMsg;
    }

    public static SingleCoinUIState successGetMarketCharts(
            String formattedPrice,
            String formattedPercentChange,
            SignedNumber signedNumber,
            List<PriceSnapshot> priceSnapshots) {

        return new SingleCoinUIState(State.SUCCESS_GET_MARKET_CHART, formattedPrice, formattedPercentChange, signedNumber, priceSnapshots);
    }

    public static SingleCoinUIState inProgress(String formattedAbbreviation, String coinName) {
        return new SingleCoinUIState(State.PROGRESS_GET_MARKET_CHART, formattedAbbreviation, coinName);
    }

    public static SingleCoinUIState failed(String errMsg) {
        return new SingleCoinUIState(State.FAILED_GET_MARKET_CHART, errMsg);
    }

    public State getState() {
        return state;
    }

    public String getFormattedAbbreviation() {
        return formattedAbbreviation;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public String getFormattedPercentChange() {
        return formattedPercentChange;
    }

    public SignedNumber getSignedNumber() {
        return signedNumber;
    }

    public List<PriceSnapshot> getPriceSnapshots() {
        return priceSnapshots;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
