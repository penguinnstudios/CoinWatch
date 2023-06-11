package com.wearos.coinwatch.ui;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wearos.coinwatch.R;
import com.wearos.coinwatch.adapter.DropDownAdapter;
import com.wearos.coinwatch.data.model.Currency;
import com.wearos.coinwatch.util.SpacingUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropDownMenu extends PopupWindow implements DropDownAdapter.OptionCallback {

    public interface SelectCurrencyCallback {
        void onSelectedCurrency(int position);
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private static final int WINDOW_HEIGHT_DP = 64;
    private final Context context;
    private final List<Currency> list;
    private final View anchorView;
    private final SelectCurrencyCallback callback;

    public DropDownMenu(
            Context context,
            List<Currency> list,
            View anchorView,
            SelectCurrencyCallback callback
    ) {
        super(context);
        this.context = context;
        this.list = list;
        this.anchorView = anchorView;
        this.callback = callback;
        setupView();
    }

    private void setupView() {
        View view = View.inflate(context, R.layout.layout_drop_down_menu, null);
        ButterKnife.bind(this, view);

        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bgr_drop_down));

        DropDownAdapter adapter = new DropDownAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        this.setWidth(anchorView.getWidth());
        this.setHeight(SpacingUtils.convertIntToDP(context, WINDOW_HEIGHT_DP));

        setContentView(view);
    }

    @Override
    public void onClickOption(int adapterPosition) {
        callback.onSelectedCurrency(adapterPosition);
        dismiss();
    }
}
