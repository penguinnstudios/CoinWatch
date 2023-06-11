package com.wearos.coinwatch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wearos.coinwatch.data.LocalRepository;
import com.wearos.coinwatch.data.RemoteRepository;
import com.wearos.coinwatch.data.SharedPrefManager;
import com.wearos.coinwatch.data.model.CoinPriceResponse;
import com.wearos.coinwatch.data.model.Coin;
import com.wearos.coinwatch.data.model.Currency;
import com.wearos.coinwatch.data.model.Favorite;
import com.wearos.coinwatch.data.model.FormattedCoin;
import com.wearos.coinwatch.data.uistate.MainUIState;
import com.wearos.coinwatch.util.Constants;
import com.wearos.coinwatch.util.HomeButton;
import com.wearos.coinwatch.util.MainRecyclerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final List<MainRecyclerItem> itemList = new ArrayList<>();
    private final MutableLiveData<MainUIState> liveData = new MutableLiveData<>();
    private final SharedPrefManager sharedPrefManager;
    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;
    private final List<Favorite> listFavorites = new ArrayList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MainViewModel(
            RemoteRepository remoteRepository,
            LocalRepository localRepository,
            SharedPrefManager sharedPrefManager
    ) {
        this.remoteRepository = remoteRepository;
        this.sharedPrefManager = sharedPrefManager;
        this.localRepository = localRepository;

        boolean isFirstTime = sharedPrefManager.isFirstTimeOpeningApp();

        if (isFirstTime) {
            Completable insertCompletable = localRepository.insertStartingCoins()
                    .doOnComplete(() -> {
                        sharedPrefManager.setFirstTimeOpeningApp(false);
                    });
            getCoinPrices(insertCompletable);
        } else {
            getCoinPrices(Completable.complete());
        }
    }

    public void getCoinPrices(Completable insertCompletable) {
        Currency currency = localRepository.getCurrencyType();
        Disposable getPriceDisposable = insertCompletable.andThen(localRepository.getFavoriteCoins())
                .flatMapObservable(favoriteCoins -> {
                    listFavorites.clear();
                    itemList.clear();

                    listFavorites.addAll(favoriteCoins);
                    itemList.add(HomeButton.create(HomeButton.Type.TITLE));
                    List<FormattedCoin> placeholders = createCoinPlaceholders(favoriteCoins, currency);
                    itemList.addAll(placeholders);
                    itemList.add(HomeButton.create(HomeButton.Type.ADD_MORE));
                    itemList.add(HomeButton.create(HomeButton.Type.SETTINGS));

                    //Syncs prices every 20 seconds. Prevents emissions if the list of favorite coins is empty.
                    return Observable.interval(0, 20, TimeUnit.SECONDS)
                            .filter(n -> !listFavorites.isEmpty());
                })
                .flatMapSingle(n -> remoteRepository.getMultipleCoinPrices(listFavorites))
                .map(apiResponse -> createFormattedCoinList(currency, listFavorites, apiResponse))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinListWithPrices -> {
                    itemList.clear();
                    itemList.add(HomeButton.create(HomeButton.Type.TITLE));
                    itemList.addAll(coinListWithPrices);
                    itemList.add(HomeButton.create(HomeButton.Type.ADD_MORE));
                    itemList.add(HomeButton.create(HomeButton.Type.SETTINGS));
                    liveData.setValue(MainUIState.successGetCoinPrices());
                }, err -> {
                    liveData.setValue(MainUIState.successCreateFavoriteCoinPlaceholders());
                    handleError(err);
                    //Retries every 20 seconds
                    Disposable retryDisposable = Observable.timer(20, TimeUnit.SECONDS)
                            .subscribe(n -> {
                                getCoinPrices(Completable.complete());
                            });
                    compositeDisposable.add(retryDisposable);
                });

        compositeDisposable.add(getPriceDisposable);
    }

    private void handleError(Throwable err) {
        if (err instanceof HttpException) {
            HttpException e = (HttpException) err;
            switch (e.code()) {
                case Constants.TOO_MANY_REQUESTS:
                    liveData.setValue(MainUIState.failed("Too many requests"));
                    return;

                case Constants.BAD_REQUEST:
                    liveData.setValue(MainUIState.failed("Bad request"));
                    return;

                case Constants.INTERNAL_SERVER_ERROR:
                    liveData.setValue(MainUIState.failed("Server error"));
                    return;

                default:
                    Timber.e(err, "Uncaught exception: %s", e.code());
            }
        } else {
            Timber.e(err, "Failed to get price history");
            liveData.setValue(MainUIState.unexpectedError());
        }
    }

    private List<FormattedCoin> createFormattedCoinList(
            Currency currency,
            List<Favorite> listFavorites,
            List<CoinPriceResponse> apiResponse) {

        final Map<String, CoinPriceResponse> map = createCoinPriceResponseMap(apiResponse);
        final List<FormattedCoin> newList = new ArrayList<>();

        for (Favorite favorite : listFavorites) {
            Coin coin = Coin.of(favorite.getCoinQueryId());

            CoinPriceResponse result = Objects.requireNonNull(map.get(favorite.getCoinQueryId()),
                    "Specified coin query id does not exist");

            FormattedCoin formattedCoin = new FormattedCoin(
                    coin, currency, result.getPrice(), result.getPercentChange());
            newList.add(formattedCoin);
        }

        return newList;
    }

    //Creates a map that helps sort the prices of the api response
    //to the corresponding order of the user's favorite coins
    private static Map<String, CoinPriceResponse> createCoinPriceResponseMap(List<CoinPriceResponse> apiResponse) {
        return apiResponse.stream().collect(
                Collectors.toMap(CoinPriceResponse::getCoinQueryId, Function.identity()));
    }

    //Creates a list of favorite coins, but without prices
    private List<FormattedCoin> createCoinPlaceholders(List<Favorite> favoriteCoins, Currency currency) {
        List<FormattedCoin> list = new ArrayList<>();
        for (Favorite favorite : favoriteCoins) {
            Coin coin = Coin.of(favorite.getCoinQueryId());
            FormattedCoin formattedCoin = new FormattedCoin(coin, currency);
            list.add(formattedCoin);
        }
        return list;
    }

    public void dispose() {
        compositeDisposable.clear();
    }

    public MutableLiveData<MainUIState> getLiveData() {
        return liveData;
    }

    public List<MainRecyclerItem> getItemList() {
        return itemList;
    }

    //Stores the selected coin in SharedPreferences when user clicks a coin
    public void saveSelectedCoin(int adapterPosition) {
        if (itemList.get(adapterPosition) instanceof FormattedCoin) {
            FormattedCoin coin = (FormattedCoin) itemList.get(adapterPosition);
            sharedPrefManager.setSelectedCoin(coin.getCoinType().getQueryId());
            sharedPrefManager.setCoinPrice(String.valueOf(coin.getPrice()));
            sharedPrefManager.setPercentChange(String.valueOf(coin.getPercentChange()));
        }
    }

    public void resumeGetCoinPrices() {
        getCoinPrices(Completable.complete());
    }
}
