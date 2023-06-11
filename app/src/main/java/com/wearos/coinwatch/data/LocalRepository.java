package com.wearos.coinwatch.data;

import com.wearos.coinwatch.data.model.Coin;
import com.wearos.coinwatch.data.model.Currency;
import com.wearos.coinwatch.data.model.Favorite;
import com.wearos.coinwatch.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class LocalRepository {

    private static final HashSet<Coin> defaultFavorites = new LinkedHashSet<>();
    private final FavoriteStatusDao favoriteStatusDao;
    private final SharedPrefManager sharedPrefManager;

    static {
        defaultFavorites.add(Coin.BITCOIN);
        defaultFavorites.add(Coin.ETHEREUM);
    }

    @Inject
    public LocalRepository(FavoriteStatusDao favoriteStatusDao, SharedPrefManager sharedPrefManager) {
        this.favoriteStatusDao = favoriteStatusDao;
        this.sharedPrefManager = sharedPrefManager;
    }

    //Sorted by coin_query_id ASC, the same as the coin gecko api
    public Single<List<Favorite>> getFavoriteCoins() {
        return favoriteStatusDao.getFavoriteCoins().map(allAvailableFavorites ->
                allAvailableFavorites.stream()
                        .filter(Favorite::isFavorite)
                        .collect(Collectors.toList()));
    }

    public Currency getCurrencyType() {
        return sharedPrefManager.getCurrencyType();
    }

    public Completable insertStartingCoins() {
        return favoriteStatusDao.insertFavorite(createListOfDefaultFavorites());
    }

    private static List<Favorite> createListOfDefaultFavorites() {
        final List<Favorite> list = new ArrayList<>();
        Coin[] coins = Coin.values();
        for (int i = 0; i < coins.length; i++) {
            if (defaultFavorites.contains(coins[i])) {
                list.add(new Favorite(coins[i].getQueryId(), true, i));
            } else {
                list.add(new Favorite(coins[i].getQueryId(), false, i));
            }
        }
        return list;
    }

    public Single<Integer> updateFavorites(Set<Coin> setFavoriteCoins) {
        final List<Coin> listConvertedFromSet = new ArrayList<>(setFavoriteCoins);
        final List<Favorite> favorites = new ArrayList<>();

        for (Coin coin : Coin.values()) {
            String coinQueryId = coin.getQueryId();

            Favorite favorite;

            if (setFavoriteCoins.contains(coin)) {
                favorite = new Favorite(coinQueryId, true, listConvertedFromSet.indexOf(coin));
            } else {
                favorite = new Favorite(coinQueryId, false, Constants.NOT_FAVORITE_COIN);
            }

            favorites.add(favorite);
        }
        return favoriteStatusDao.updateFavorites(favorites);
    }

    //The coin passed from MainActivity to SingleCoinActivity after clicking on item in the recycler
    public Coin getSelectedCoin() {
        String coinQueryId = sharedPrefManager.getSelectedCoin();
        return Coin.of(coinQueryId);
    }
}
