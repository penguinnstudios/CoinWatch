package com.wearos.coinwatch.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "favorites")
public class Favorite {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "coin_query_id")
    private String coinQueryId;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @ColumnInfo(name = "favorite_order")
    private int favoriteOrder;

    public Favorite(@NotNull String coinQueryId, boolean isFavorite, int favoriteOrder) {
        this.coinQueryId = coinQueryId;
        this.isFavorite = isFavorite;
        this.favoriteOrder = favoriteOrder;
    }

    @NonNull
    public String getCoinQueryId() {
        return coinQueryId;
    }

    public void setCoinQueryId(@NonNull String coinQueryId) {
        this.coinQueryId = coinQueryId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getFavoriteOrder() {
        return favoriteOrder;
    }

    public void setFavoriteOrder(int favoriteOrder) {
        this.favoriteOrder = favoriteOrder;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "coinQueryId='" + coinQueryId + '\'' +
                ", isFavorite=" + isFavorite +
                ", favoriteOrder=" + favoriteOrder +
                '}';
    }
}
