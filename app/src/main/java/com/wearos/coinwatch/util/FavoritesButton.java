package com.wearos.coinwatch.util;

public class FavoritesButton implements FavoritesRecyclerItem {

    public enum Type {
        EMPTY_PLACEHOLDER,
        TITLE
    }

    private final Type type;

    private FavoritesButton(Type type) {
        this.type = type;
    }

    public static FavoritesButton create(Type type) {
        return new FavoritesButton(type);
    }

    public Type getType() {
        return type;
    }
}
