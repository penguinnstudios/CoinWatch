package com.wearos.coinwatch.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wearos.coinwatch.R;
import com.wearos.coinwatch.data.model.MarketChartType;
import com.wearos.coinwatch.data.model.PriceSnapshot;
import com.wearos.coinwatch.data.model.SignedNumber;
import com.wearos.coinwatch.util.ChartMarker;
import com.wearos.coinwatch.viewmodels.SingleCoinViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SingleCoinActivity extends ComponentActivity {

    @BindView(R.id.linechart)
    LineChart lineChart;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    @BindView(R.id.tv_percent_change)
    TextView tvPercentChange;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_abbreviation)
    TextView tvAbbreviation;

    @BindView(R.id.radiogroup)
    RadioGroup radioGroup;

    @BindView(R.id.radio_btn_day)
    RadioButton radioBtnDay;

    @BindView(R.id.radio_btn_week)
    RadioButton radioBtnWeek;

    @BindView(R.id.radio_btn_month)
    RadioButton radioBtnMonth;

    @BindView(R.id.radio_btn_year)
    RadioButton radioBtnYear;

    private SingleCoinViewModel viewModel;
    private ChartMarker chartMarkerIndexZero, chartMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_coin_activity);
        ButterKnife.bind(this);

        viewModel = new ViewModelProvider(this).get(SingleCoinViewModel.class);
        viewModel.getLiveData().observe(this, state -> {
            switch (state.getState()) {
                case SUCCESS_GET_MARKET_CHART:
                    lineChart.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    tvPrice.setText(state.getFormattedPrice());
                    tvPercentChange.setText(state.getFormattedPercentChange());
                    updateColorOfPercentChange(state.getSignedNumber());
                    addLineChartData(state.getPriceSnapshots());
                    enableRadioBtns();
                    break;

                case PROGRESS_GET_MARKET_CHART:
                    lineChart.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    tvName.setText(state.getCoinName());
                    tvAbbreviation.setText(state.getFormattedAbbreviation());
                    disableRadioBtns();
                    break;

                case FAILED_GET_MARKET_CHART:
                    lineChart.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    //line.clear() allows the no data text to show. dataSet.clear() doesnt.
                    lineChart.clear();
                    lineChart.setNoDataText(state.getErrMsg());
                    lineChart.invalidate();
                    enableRadioBtns();
                    break;
            }

        });

        lineChart.setMinOffset(0f);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        //If you don't set no data text to null, you get no chart data available
        lineChart.setNoDataText(null);
        lineChart.setScaleEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setMarker(chartMarker);
        lineChart.setOnChartValueSelectedListener(valueListener);
        lineChart.setOnChartGestureListener(gestureListener);

        //Marker for all data points except the first
        chartMarker = new ChartMarker(this, R.layout.line_chart_marker);

        //Used for the first data point because it's cut off by default
        chartMarkerIndexZero = new ChartMarker(this, R.layout.line_chart_marker_index_zero);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSelectedRadioBtn();
        viewModel.getPriceHistory();
    }

    private void addLineChartData(List<PriceSnapshot> priceSnapshots) {
        List<Entry> entries = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entries, null);

        for (int i = 0; i < priceSnapshots.size(); i++) {
            Entry entry = new Entry(i, (float) priceSnapshots.get(i).getPrice());
            dataSet.addEntry(entry);
        }

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient_purple);
        dataSet.setFillDrawable(drawable);
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setHighLightColor(Color.WHITE);
        dataSet.setHighlightLineWidth(1.5f);
        dataSet.setColor(ContextCompat.getColor(this, R.color.purple_200));
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private final OnChartValueSelectedListener valueListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {
            int index = (int) h.getX();
            tvPrice.setText(viewModel.getList().get(index).getFormattedPrice());
            tvPercentChange.setText(viewModel.getList().get(index).getFormattedDate());
            tvPercentChange.setTextColor(ContextCompat.getColor(
                    SingleCoinActivity.this, R.color.default_text_color));

            if (index == 0) {
                lineChart.setMarker(chartMarkerIndexZero);
            } else {
                lineChart.setMarker(chartMarker);
            }

            lineChart.invalidate();
        }

        @Override
        public void onNothingSelected() {

        }
    };

    private final OnChartGestureListener gestureListener = new OnChartGestureListener() {
        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            tvPercentChange.setText(viewModel.getFormattedPercentChange());
            updateColorOfPercentChange(viewModel.getSignedNumber());
            tvPrice.setText(viewModel.getFormattedPrice());
            lineChart.highlightValues(null);
        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }
    };

    private void updateColorOfPercentChange(SignedNumber signedNumber) {
        switch (signedNumber) {
            case POSITIVE:
                tvPercentChange.setTextColor(ContextCompat.getColor(this, R.color.green_500));
                break;
            case NEGATIVE:
            case ZERO:
                tvPercentChange.setTextColor(ContextCompat.getColor(this, R.color.default_text_color));
                break;
        }
    }

    private void disableRadioBtns() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void enableRadioBtns() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.dispose();
    }

    @OnClick(R.id.layout_top_chin)
    void onBack(){
        finish();
    }

    @OnClick(R.id.radio_btn_day)
    void onDay() {
        viewModel.setMarketChartInterval(MarketChartType.DAY);
        checkSelectedRadioBtn();
        viewModel.getPriceHistory();
    }

    @OnClick(R.id.radio_btn_week)
    void onWeek() {
        viewModel.setMarketChartInterval(MarketChartType.WEEK);
        checkSelectedRadioBtn();
        viewModel.getPriceHistory();
    }

    @OnClick(R.id.radio_btn_month)
    void onMonth() {
        viewModel.setMarketChartInterval(MarketChartType.MONTH);
        checkSelectedRadioBtn();
        viewModel.getPriceHistory();
    }

    @OnClick(R.id.radio_btn_year)
    void onYear() {
        viewModel.setMarketChartInterval(MarketChartType.YEAR);
        checkSelectedRadioBtn();
        viewModel.getPriceHistory();
    }

    private void checkSelectedRadioBtn() {
        RadioButton radioBtn = (RadioButton) radioGroup.getChildAt(
                viewModel.getMarketChartType().getRadioGroupIndex());
        radioBtn.setChecked(true);
    }
}
