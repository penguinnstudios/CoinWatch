package com.wearos.coinwatch.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.wearos.coinwatch.R;
import com.wearos.coinwatch.viewmodels.SettingsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends ComponentActivity implements DropDownMenu.SelectCurrencyCallback {

    @BindView(R.id.spinner)
    TextView spinner;

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.getLiveData().observe(this, settingsUIState -> {
            switch (settingsUIState.getState()) {
                case SUCCESS_UPDATE_CURRENCY:
                    spinner.setText(settingsUIState.getCurrencyName());

                case SUCCESS_GET_CURRENCY:
                    spinner.setText(settingsUIState.getCurrencyName());
                    break;
            }
        });
        viewModel.getSelectedCurrency();
    }

    @OnClick(R.id.spinner)
    void onSpinnerCurrency() {
        DropDownMenu dropDownMenu = new DropDownMenu(
                this, viewModel.getCurrencyList(), spinner, this);
        dropDownMenu.showAsDropDown(spinner, 0, 0);
    }

    @Override
    public void onSelectedCurrency(int position) {
        viewModel.setCurrencyType(position);
    }
}
