package com.wearos.coinwatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wearos.coinwatch.R;
import com.wearos.coinwatch.data.model.FormattedCoin;
import com.wearos.coinwatch.util.HomeButton;
import com.wearos.coinwatch.util.MainRecyclerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinListAdapter extends ListAdapter<MainRecyclerItem, WearableRecyclerView.ViewHolder> {

    public interface ItemClickCallback {
        void onAddBtnClick();

        void onSettingsBtnClick();

        void onCoinClick(int adapterPosition);
    }

    private static final float VIEW_HEIGHT = 0.4f;
    private static final int LABEL_TITLE = 0;
    private static final int ADD_BTN = 1;
    private static final int SETTINGS_BTN = 2;
    private static final int COIN = 3;

    private final ItemClickCallback callback;
    private int colorGreen, colorGrey;

    public CoinListAdapter(ItemClickCallback callback) {
        super(new CoinDiffCallback());
        this.callback = callback;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Context context = recyclerView.getContext();
        colorGreen = ContextCompat.getColor(context, R.color.green_500);
        colorGrey = ContextCompat.getColor(context, R.color.default_text_color);
    }

    @Override
    public int getItemViewType(int position) {
        MainRecyclerItem mainRecyclerItem = getItem(position);
        if (mainRecyclerItem instanceof HomeButton) {
            switch (((HomeButton) mainRecyclerItem).getType()) {
                case TITLE:
                    return LABEL_TITLE;
                case ADD_MORE:
                    return ADD_BTN;
                case SETTINGS:
                    return SETTINGS_BTN;
            }
        }
        return COIN;
    }

    @NonNull
    @Override
    public WearableRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LABEL_TITLE:
                return new TitleViewHolder(inflateView(R.layout.viewholder_title, parent));
            case ADD_BTN:
                return new AddCoinViewHolder(inflateView(R.layout.viewholder_add_coin, parent));
            case SETTINGS_BTN:
                return new SettingsViewHolder(inflateView(R.layout.viewholder_settings, parent));
            case COIN:
                return new CoinViewHolder(inflateView(R.layout.viewholder_coin, parent));
            default:
                throw new IllegalStateException("Invalid view holder type");
        }
    }

    private View inflateView(int layoutId, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * VIEW_HEIGHT);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull WearableRecyclerView.ViewHolder holder, int i) {
        if (holder.getItemViewType() == COIN) {
            FormattedCoin formattedCoin = (FormattedCoin) getItem(i);
            CoinViewHolder coinViewHolder = (CoinViewHolder) holder;

            Glide.with(holder.itemView.getContext())
                    .load(formattedCoin.getCoinType().getIcon())
                    .apply(RequestOptions.circleCropTransform())
                    .into(coinViewHolder.ivIcon);

            coinViewHolder.tvAbbreviation.setText(formattedCoin.getCoinType().getAbbreviation());
            coinViewHolder.tvPrice.setText(formattedCoin.getFormattedPrice());
            coinViewHolder.tvPercentChange.setText(formattedCoin.getFormattedPercentChange());

            switch (formattedCoin.getSignedNumber()) {
                case POSITIVE:
                    coinViewHolder.tvPercentChange.setTextColor(colorGreen);
                    break;

                case NEGATIVE:

                case ZERO:
                    coinViewHolder.tvPercentChange.setTextColor(colorGrey);
                    break;
            }
        }
    }

    public class AddCoinViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {
        public AddCoinViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onAddBtnClick();
        }
    }

    public class SettingsViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {
        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            callback.onSettingsBtnClick();
        }
    }

    public class CoinViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_abbreviation)
        TextView tvAbbreviation;

        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        @BindView(R.id.tv_percent_change)
        TextView tvPercentChange;

        @BindView(R.id.tv_price)
        TextView tvPrice;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onCoinClick(getAdapterPosition());
        }
    }

    public static class TitleViewHolder extends WearableRecyclerView.ViewHolder {
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class CoinDiffCallback extends DiffUtil.ItemCallback<MainRecyclerItem> {
        @Override
        public boolean areItemsTheSame(@NonNull MainRecyclerItem oldItem, @NonNull MainRecyclerItem newItem) {
            if (oldItem instanceof FormattedCoin && newItem instanceof FormattedCoin) {
                FormattedCoin oldCoin = (FormattedCoin) oldItem;
                FormattedCoin newCoin = (FormattedCoin) newItem;
                return oldCoin.getCoinType() == newCoin.getCoinType();
            }
            return true;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MainRecyclerItem oldItem, @NonNull MainRecyclerItem newItem) {
            if (oldItem instanceof FormattedCoin && newItem instanceof FormattedCoin) {
                FormattedCoin oldCoin = (FormattedCoin) oldItem;
                FormattedCoin newCoin = (FormattedCoin) newItem;
                return oldCoin.getFormattedPrice().equals(newCoin.getFormattedPrice()) &&
                        oldCoin.getFormattedPercentChange().equals(newCoin.getFormattedPercentChange());
            }
            return true;
        }
    }
}
