package com.wearos.coinwatch.data;

import com.wearos.coinwatch.data.model.CoinPriceResponse;
import com.wearos.coinwatch.data.model.MarketChartResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinGeckoService {

    @Headers("accept: application/json")
    @GET("coins/markets")
    Single<List<CoinPriceResponse>> getCoins(
            @Query("vs_currency") String vsCurrency,
            @Query("ids") String ids
    );

    @Headers("accept: application/json")
    @GET("coins/{coin_id}/market_chart")
    Single<MarketChartResponse> getMarketChart(
            @Path("coin_id") String coinId,
            @Query("vs_currency") String vsCurrency,
            @Query("days") int days,
            @Query("interval") String interval
    );
}
