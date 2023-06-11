package com.wearos.coinwatch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wearos.coinwatch.data.SharedPrefManager;
import com.wearos.coinwatch.data.model.Currency;
import com.wearos.coinwatch.data.uistate.SettingsUIState;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final List<Currency> currencyList = Arrays.asList(Currency.values());
    private final MutableLiveData<SettingsUIState> liveData = new MutableLiveData<>();
    private final SharedPrefManager sharedPrefManager;

    @Inject
    public SettingsViewModel(SharedPrefManager sharedPrefManager) {
        this.sharedPrefManager = sharedPrefManager;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyType(int adapterPosition) {
        Currency currency = currencyList.get(adapterPosition);
        sharedPrefManager.setCurrencyType(currency.getCurrencyQueryId());
        liveData.setValue(SettingsUIState.successUpdateCurrency(currency.getCurrencyName()));
    }

    public MutableLiveData<SettingsUIState> getLiveData() {
        return liveData;
    }

    public void getSelectedCurrency() {
        Currency currency = sharedPrefManager.getCurrencyType();
        liveData.setValue(SettingsUIState.successGetCurrency(currency.getCurrencyName()));
    }
}
