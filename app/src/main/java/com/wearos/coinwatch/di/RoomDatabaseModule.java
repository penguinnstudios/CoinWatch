package com.wearos.coinwatch.di;

import android.content.Context;

import androidx.room.Room;

import com.wearos.coinwatch.data.AppDatabase;
import com.wearos.coinwatch.data.FavoriteStatusDao;
import com.wearos.coinwatch.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomDatabaseModule {

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, Constants.DB_NAME)
                .fallbackToDestructiveMigration() //Clear database upon version upgrade
                .build();
    }

    @Singleton
    @Provides
    FavoriteStatusDao coinDao(AppDatabase appDatabase) {
        return appDatabase.favoriteStatusDao();
    }
}
