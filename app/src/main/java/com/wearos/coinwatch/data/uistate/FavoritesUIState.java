package com.wearos.coinwatch.data.uistate;

public class FavoritesUIState {

    public enum State {
        SUCCESS_GET_FAVORITES,
        ON_FAVORITE_CLICKED
    }

    private final State state;
    private int adapterPosition;

    private FavoritesUIState(State state) {
        this.state = state;
    }

    private FavoritesUIState(State state, int adapterPosition) {
        this.state = state;
        this.adapterPosition = adapterPosition;
    }

    public static FavoritesUIState successGetFavorites() {
        return new FavoritesUIState(State.SUCCESS_GET_FAVORITES);
    }

    public static FavoritesUIState onFavoriteClicked(int adapterPosition) {
        return new FavoritesUIState(State.ON_FAVORITE_CLICKED, adapterPosition);
    }

    public State getType() {
        return state;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }
}
