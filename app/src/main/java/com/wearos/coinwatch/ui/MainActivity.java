package com.wearos.coinwatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.wearos.coinwatch.R;
import com.wearos.coinwatch.adapter.CoinListAdapter;
import com.wearos.coinwatch.adapter.ProgressAdapter;
import com.wearos.coinwatch.util.CustomScroll;
import com.wearos.coinwatch.util.ProgressPlaceholder;
import com.wearos.coinwatch.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends ComponentActivity implements CoinListAdapter.ItemClickCallback {

    @BindView(R.id.recycler_view)
    WearableRecyclerView coinListRecycler;

    @BindView(R.id.progress_recycler_View)
    WearableRecyclerView progressRecycler;

    private CoinListAdapter coinListAdapter;
    private MainViewModel viewModel;
    private boolean isNewInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<ProgressPlaceholder> progressPlaceholders = new ArrayList<>();
        progressPlaceholders.add(ProgressPlaceholder.PROGRESS_ANIM);
        progressPlaceholders.add(ProgressPlaceholder.PROGRESS_LAYOUT);
        progressPlaceholders.add(ProgressPlaceholder.PROGRESS_LAYOUT);
        ProgressAdapter progressAdapter = new ProgressAdapter(progressPlaceholders);
        progressRecycler.setLayoutManager(new WearableLinearLayoutManager(this, new CustomScroll()));
        progressRecycler.setAdapter(progressAdapter);

        //setItemAnimator is set to null because DiffUtil has a bug that causes app to crash when returning from
        //FavoritesActivity due to the animation.
        coinListAdapter = new CoinListAdapter(this);
        coinListRecycler.setLayoutManager(new WearableLinearLayoutManager(this, new CustomScroll()));
        coinListRecycler.setItemAnimator(null);
        coinListRecycler.setAdapter(coinListAdapter);

        //Makes view holders snap to center of screen
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(coinListRecycler);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getLiveData().observe(this, state -> {
            switch (state.getType()) {
                case CREATE_FAVORITE_COIN_PLACEHOLDERS:

                case SUCCESS_GET_PRICES:
                    coinListAdapter.submitList(new ArrayList<>(viewModel.getItemList()));
                    displayMainLayout();
                    break;

                case FAILED_GET_PRICES:
                    displayMainLayout();
                    Toast.makeText(this, state.getErrMsg(), Toast.LENGTH_SHORT).show();
                    break;

                case UNEXPECTED_ERROR:
                    displayMainLayout();
                    break;
            }
        });

        isNewInstance = true;
    }

    private void displayMainLayout() {
        new Handler().postDelayed(() -> {
            coinListRecycler.setVisibility(View.VISIBLE);
            progressRecycler.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                progressRecycler.setVisibility(View.GONE);
            });
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Prevents getCoinPrices being called twice
        if (isNewInstance) {
            isNewInstance = false;
        } else {
            viewModel.resumeGetCoinPrices();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.dispose();
    }

    @Override
    public void onAddBtnClick() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSettingsBtnClick() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCoinClick(int adapterPosition) {
        viewModel.saveSelectedCoin(adapterPosition);
        Intent intent = new Intent(this, SingleCoinActivity.class);
        startActivity(intent);
    }
}