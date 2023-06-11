package com.wearos.coinwatch.util;

public enum ProgressPlaceholder {

    PROGRESS_ANIM(0),
    PROGRESS_LAYOUT(1);

    private final int itemViewType;

    ProgressPlaceholder(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public int getItemViewType() {
        return itemViewType;
    }
}
