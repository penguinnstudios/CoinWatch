package com.wearos.coinwatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wearos.coinwatch.R;
import com.wearos.coinwatch.data.model.Currency;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropDownAdapter extends RecyclerView.Adapter<DropDownAdapter.ViewHolder> {

    public interface OptionCallback {
        void onClickOption(int adapterPosition);
    }

    private final List<Currency> list;
    private final OptionCallback callback;

    public DropDownAdapter(List<Currency> list, OptionCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_dropdown_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tvOption.setText(list.get(i).getCurrencyName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_option)
        TextView tvOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onClickOption(getAdapterPosition());
        }
    }
}
