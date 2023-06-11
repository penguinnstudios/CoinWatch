package com.wearos.coinwatch.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wearos.coinwatch.data.model.Favorite;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoriteStatusDao favoriteStatusDao();
}
