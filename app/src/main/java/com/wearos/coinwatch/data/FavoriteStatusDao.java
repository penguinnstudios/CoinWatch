package com.wearos.coinwatch.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wearos.coinwatch.data.model.Favorite;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteStatusDao {

    //Default return type is a Integer
    //If I have 4 checked, it returns the Integer 4, the number of rows updated
    @Update
    Single<Integer> updateFavorites(List<Favorite> favorites);

    //Default return type is a Long if it's one Object, but if using a List<> it's a List of Longs
    //[-1, -1, -1] means the insert failed
    //Success inserting default coins into favorites
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertFavorite(List<Favorite> favorites);

    @Query("SELECT * FROM favorites ORDER BY favorite_order ASC")
    Single<List<Favorite>> getFavoriteCoins();
}
