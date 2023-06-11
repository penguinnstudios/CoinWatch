package com.wearos.coinwatch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wearos.coinwatch.data.LocalRepository;
import com.wearos.coinwatch.data.model.Coin;
import com.wearos.coinwatch.data.model.Favorite;
import com.wearos.coinwatch.data.model.FormattedFavorite;
import com.wearos.coinwatch.data.uistate.FavoritesUIState;
import com.wearos.coinwatch.util.FavoritesButton;
import com.wearos.coinwatch.util.FavoritesRecyclerItem;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class FavoritesViewModel extends ViewModel {

    private final LocalRepository localRepository;
    private final List<FavoritesRecyclerItem> itemList = new ArrayList<>();
    private final MutableLiveData<FavoritesUIState> liveData = new MutableLiveData<>();

    private final Set<Coin> setFavoriteCoins = new LinkedHashSet<>();
    private Disposable getFavoritesDisposable, updateFavoriteDisposable;

    @Inject
    public FavoritesViewModel(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public void getFavorites() {
        itemList.add(FavoritesButton.create(FavoritesButton.Type.TITLE));
        itemList.addAll(createFormattedListOfAvailableCoins());
        itemList.add(FavoritesButton.create(FavoritesButton.Type.EMPTY_PLACEHOLDER));

        getFavoritesDisposable = localRepository.getFavoriteCoins()
                .map(listFavorites -> {
                    addFavoriteCoinsToMap(listFavorites);
                    return listFavorites;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listFavorites -> {
                    liveData.setValue(FavoritesUIState.successGetFavorites());
                }, err -> {
                    Timber.e(err, "Failed to get favorites");
                });
    }

    //Adds only the user's favorite coins into a ordered map
    private void addFavoriteCoinsToMap(List<Favorite> listFavorites) {
        for (Favorite favorite : listFavorites) {
            setFavoriteCoins.add(Coin.of(favorite.getCoinQueryId()));
        }
    }

    public List<FormattedFavorite> createFormattedListOfAvailableCoins() {
        List<FormattedFavorite> availableCoins = new ArrayList<>();
        for (Coin coin : Coin.values()) {
            FormattedFavorite formattedFavorite = new FormattedFavorite(coin);
            availableCoins.add(formattedFavorite);
        }
        return availableCoins;
    }

    public void onFavoriteItemClick(int adapterPosition) {
        FormattedFavorite formattedFavorite = (FormattedFavorite) itemList.get(adapterPosition);
        Coin coin = formattedFavorite.getCoinType();

        if (setFavoriteCoins.contains(coin)) {
            setFavoriteCoins.remove(coin);
        } else {
            setFavoriteCoins.add(coin);
        }

        updateFavorites(adapterPosition);
    }

    private void updateFavorites(int adapterPosition) {
        updateFavoriteDisposable = localRepository.updateFavorites(setFavoriteCoins)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(numRowsUpdated -> {
                    liveData.setValue(FavoritesUIState.onFavoriteClicked(adapterPosition));
                }, err -> {
                    Timber.e(err, "Failed to update favorites");
                });
    }

    public Set<Coin> getMapFavoriteCoins() {
        return setFavoriteCoins;
    }

    public MutableLiveData<FavoritesUIState> getLiveData() {
        return liveData;
    }

    public List<FavoritesRecyclerItem> getItemList() {
        return itemList;
    }

    public void dispose() {
        if (getFavoritesDisposable != null) {
            getFavoritesDisposable.dispose();
        }

        if (updateFavoriteDisposable != null) {
            updateFavoriteDisposable.dispose();
        }
    }
}
