package com.wearos.coinwatch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wearos.coinwatch.data.LocalRepository;
import com.wearos.coinwatch.data.RemoteRepository;
import com.wearos.coinwatch.data.SharedPrefManager;
import com.wearos.coinwatch.data.model.Coin;
import com.wearos.coinwatch.data.model.Currency;
import com.wearos.coinwatch.data.model.MarketChartResponse;
import com.wearos.coinwatch.data.model.MarketChartType;
import com.wearos.coinwatch.data.model.PriceSnapshot;
import com.wearos.coinwatch.data.model.SignedNumber;
import com.wearos.coinwatch.data.uistate.SingleCoinUIState;
import com.wearos.coinwatch.util.Constants;
import com.wearos.coinwatch.util.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

@HiltViewModel
public class SingleCoinViewModel extends ViewModel {

    private final SharedPrefManager sharedPrefManager;
    private final RemoteRepository remoteRepository;
    private final LocalRepository localRepository;
    private final MutableLiveData<SingleCoinUIState> liveData = new MutableLiveData<>();
    private final List<PriceSnapshot> list = new ArrayList<>();
    private final Currency currency;
    private final SignedNumber signedNumber;
    private final String formattedPrice;
    private final String formattedPercentChange;
    private MarketChartType marketChartType = MarketChartType.WEEK;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public SingleCoinViewModel(
            RemoteRepository remoteRepository,
            LocalRepository localRepository,
            SharedPrefManager sharedPrefManager
    ) {
        this.currency = sharedPrefManager.getCurrencyType();
        double percentChange = Double.parseDouble(sharedPrefManager.getPercentChange());
        double currentPrice = Double.parseDouble(sharedPrefManager.getCoinPrice());
        this.signedNumber = FormatUtils.convertToSignedNumber(percentChange);
        this.formattedPercentChange = FormatUtils.formatPercentChange(signedNumber, percentChange);
        this.formattedPrice = FormatUtils.formatPrice(currency, currentPrice);

        this.remoteRepository = remoteRepository;
        this.localRepository = localRepository;
        this.sharedPrefManager = sharedPrefManager;
    }

    public void getPriceHistory() {
        Coin coin = localRepository.getSelectedCoin();
        Currency currency = sharedPrefManager.getCurrencyType();
        String formattedAbbreviation = coin.getAbbreviation();

        liveData.setValue(SingleCoinUIState.inProgress(formattedAbbreviation, coin.getName()));

        Disposable disposable = remoteRepository.getMarketChart(coin.getQueryId(), currency.getCurrencyQueryId(),
                marketChartType.getDays(), marketChartType.getMarketInterval())
                .map(queryResult -> {
                    list.clear();
                    List<PriceSnapshot> listPriceSnapshots =
                            formatResponseForLineChart(queryResult, currency, marketChartType);
                    list.addAll(listPriceSnapshots);
                    return listPriceSnapshots;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listPriceSnapshots -> {
                    liveData.setValue(SingleCoinUIState.successGetMarketCharts(
                            formattedPrice, formattedPercentChange, signedNumber, listPriceSnapshots));
                }, err -> {
                    handleError(err);
                });

        compositeDisposable.add(disposable);
    }

    private void handleError(Throwable err) {
        if (err instanceof HttpException) {
            HttpException e = (HttpException) err;
            switch (e.code()) {
                case Constants.TOO_MANY_REQUESTS:
                    liveData.setValue(SingleCoinUIState.failed("Too many requests"));
                    return;

                case Constants.BAD_REQUEST:
                    liveData.setValue(SingleCoinUIState.failed("Bad request"));
                    return;

                case Constants.INTERNAL_SERVER_ERROR:
                    liveData.setValue(SingleCoinUIState.failed("Server error"));
                    return;

                default:
                    Timber.e(err, "Uncaught exception: %s", e.code());
            }
        } else {
            Timber.e(err, "Failed to get market chart");
            liveData.setValue(SingleCoinUIState.failed("Failed to get price history"));
        }
    }

    public List<PriceSnapshot> formatResponseForLineChart(
            MarketChartResponse apiResponse,
            Currency currency,
            MarketChartType marketChartType) {

        List<List<Double>> outerList = apiResponse.getPrices();
        List<PriceSnapshot> list = new ArrayList<>();
        for (int i = 0; i < outerList.size(); i++) {
            List<Double> innerList = outerList.get(i);
            list.add(new PriceSnapshot(innerList.get(0), innerList.get(1), currency, marketChartType));
        }
        return list;
    }

    public MutableLiveData<SingleCoinUIState> getLiveData() {
        return liveData;
    }

    public List<PriceSnapshot> getList() {
        return list;
    }

    public Currency getCurrencyType() {
        return currency;
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

    public void dispose() {
        compositeDisposable.clear();
    }

    public void setMarketChartInterval(MarketChartType marketChartType) {
        this.marketChartType = marketChartType;
    }

    public MarketChartType getMarketChartType() {
        return marketChartType;
    }

}
