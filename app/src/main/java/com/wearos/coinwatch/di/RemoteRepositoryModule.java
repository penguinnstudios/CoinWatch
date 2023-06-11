package com.wearos.coinwatch.di;

import com.wearos.coinwatch.data.CoinGeckoService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class RemoteRepositoryModule {

    @Provides
    @Singleton
    public CoinGeckoService provideMainService(Retrofit retrofit){
        return retrofit.create(CoinGeckoService.class);
    }
}
