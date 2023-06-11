package com.wearos.coinwatch.data;

import com.wearos.coinwatch.data.model.CoinPriceResponse;
import com.wearos.coinwatch.data.model.Favorite;
import com.wearos.coinwatch.data.model.MarketChartResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RemoteRepository {

    private final CoinGeckoService coinGeckoService;
    private final SharedPrefManager sharedPrefManager;

    @Inject
    public RemoteRepository(CoinGeckoService coinGeckoService, SharedPrefManager sharedPrefManager) {
        this.sharedPrefManager = sharedPrefManager;
        this.coinGeckoService = coinGeckoService;
    }

    public Single<List<CoinPriceResponse>> getMultipleCoinPrices(List<Favorite> listFavorites) {
        String currencyName = sharedPrefManager.getCurrencyType().getCurrencyQueryId();
        return coinGeckoService.getCoins(currencyName, formatQueryParams(listFavorites));
    }

    private static String formatQueryParams(List<Favorite> listFavorites) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listFavorites.size(); i++) {
            String coinQueryId = listFavorites.get(i).getCoinQueryId();
            sb.append(coinQueryId);
            if (i != listFavorites.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public Single<MarketChartResponse> getMarketChart(String coinId, String vsCurrency, int days, String marketInterval) {
        return coinGeckoService.getMarketChart(coinId, vsCurrency, days, marketInterval);
    }
}
