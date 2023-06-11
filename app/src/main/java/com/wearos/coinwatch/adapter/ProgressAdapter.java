package com.wearos.coinwatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.wearos.coinwatch.R;
import com.wearos.coinwatch.util.ProgressPlaceholder;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final float VIEW_HOLDER_HEIGHT = 0.33f;
    private final List<ProgressPlaceholder> list;

    public ProgressAdapter(List<ProgressPlaceholder> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        ProgressPlaceholder progressRecyclerItem = list.get(position);
        switch (progressRecyclerItem) {
            case PROGRESS_ANIM:
            case PROGRESS_LAYOUT:
                return progressRecyclerItem.getItemViewType();
            default:
                throw new IllegalStateException("Invalid item type");
        }
    }

    @NonNull
    @Override
    public WearableRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ProgressPlaceholder.PROGRESS_ANIM.getItemViewType()) {
            return new AnimViewHolder(inflateView(R.layout.viewholder_progress_anim, parent));
        } else if (viewType == ProgressPlaceholder.PROGRESS_LAYOUT.getItemViewType()) {
            return new LayoutViewHolder(inflateView(R.layout.viewholder_empty_coin_layout, parent));
        } else {
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AnimViewHolder extends WearableRecyclerView.ViewHolder {
        public AnimViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class LayoutViewHolder extends WearableRecyclerView.ViewHolder {
        public LayoutViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
