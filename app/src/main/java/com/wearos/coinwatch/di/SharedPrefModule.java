package com.wearos.coinwatch.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.wearos.coinwatch.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SharedPrefModule {

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences(
                context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE
        );
    }
}
