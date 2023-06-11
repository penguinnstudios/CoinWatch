package com.wearos.coinwatch.data.model;

import com.wearos.coinwatch.util.FavoritesRecyclerItem;

public class FormattedFavorite implements FavoritesRecyclerItem {

    private final Coin coin;

    public FormattedFavorite(Coin coin) {
        this.coin = coin;
    }

    public Coin getCoinType() {
        return coin;
    }
}
