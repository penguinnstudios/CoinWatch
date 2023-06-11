package com.wearos.coinwatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wearos.coinwatch.R;
import com.wearos.coinwatch.data.model.Coin;
import com.wearos.coinwatch.data.model.FormattedFavorite;
import com.wearos.coinwatch.util.FavoritesButton;
import com.wearos.coinwatch.util.FavoritesRecyclerItem;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClickCallback {
        void onFavoritesClick(int adapterPosition);
    }

    private static final float VIEW_HOLDER_HEIGHT = 0.33f;
    private static final int LABEL_TITLE = 0;
    private static final int EMPTY = 1;
    private static final int FAVORITE = 2;

    private final List<FavoritesRecyclerItem> list;
    private final Set<Coin> setFavoriteCoins;
    private final ItemClickCallback callback;

    public FavoritesAdapter(
            List<FavoritesRecyclerItem> list,
            Set<Coin> setFavoriteCoins,
            ItemClickCallback callback
    ) {
        this.list = list;
        this.callback = callback;
        this.setFavoriteCoins = setFavoriteCoins;
    }

    @Override
    public int getItemViewType(int position) {
        FavoritesRecyclerItem favoritesRecyclerItem = list.get(position);
        if (favoritesRecyclerItem instanceof FavoritesButton) {
            switch (((FavoritesButton) favoritesRecyclerItem).getType()) {
                case TITLE:
                    return LABEL_TITLE;
                case EMPTY_PLACEHOLDER:
                    return EMPTY;
            }
        }
        return FAVORITE;
    }

    @NonNull
    @Override
    public WearableRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LABEL_TITLE:
                return new TitleViewHolder(inflateView(R.layout.viewholder_favorites_title, parent));
            case EMPTY:
                return new SaveViewHolder(inflateView(R.layout.viewholder_empty, parent));
            case FAVORITE:
                return new FavoriteViewHolder(inflateView(R.layout.viewholder_favorite, parent));
            default:
                throw new IllegalStateException("Invalid view holder type");
        }
    }

    private View inflateView(int layoutId, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * VIEW_HOLDER_HEIGHT);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull WearableRecyclerView.ViewHolder holder, int i) {
        if (holder.getItemViewType() == FAVORITE) {
            FormattedFavorite formattedFavorite = (FormattedFavorite) list.get(i);
            FavoriteViewHolder favoriteViewHolder = (FavoriteViewHolder) holder;

            Glide.with(holder.itemView.getContext())
                    .load(formattedFavorite.getCoinType().getIcon())
                    .apply(RequestOptions.circleCropTransform())
                    .into(favoriteViewHolder.ivIcon);

            favoriteViewHolder.tvName.setText(formattedFavorite.getCoinType().getName());
            favoriteViewHolder.checkBox.setChecked(setFavoriteCoins.contains(formattedFavorite.getCoinType()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavoriteViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            callback.onFavoritesClick(adapterPosition);
        }
    }

    public static class TitleViewHolder extends WearableRecyclerView.ViewHolder {
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class SaveViewHolder extends WearableRecyclerView.ViewHolder {
        public SaveViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
