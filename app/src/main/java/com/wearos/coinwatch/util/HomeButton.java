package com.wearos.coinwatch.util;

public class HomeButton implements MainRecyclerItem {

    public enum Type {
        ADD_MORE,
        SETTINGS,
        TITLE
    }

    private final Type type;

    private HomeButton(Type type) {
        this.type = type;
    }

    public static HomeButton create(Type type) {
        return new HomeButton(type);
    }

    public Type getType() {
        return type;
    }
}
