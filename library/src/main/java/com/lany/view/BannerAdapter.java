package com.lany.view;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public abstract class BannerAdapter<T> {
    private List<T> mItems = new ArrayList<>();

    public List<T> getItems() {
        return mItems;
    }

    public BannerAdapter(List<T> items) {
        mItems = items;
    }

    public void bind(ImageView bannerImg, TextView tv, int position) {
        if (mItems != null && mItems.size() > 0) {
            bindData(bannerImg, tv, mItems.get(position));
        }
    }

    public void selectClicked(int position) {
        if (mItems != null && mItems.size() > 0) {
            onItemClicked(position, mItems.get(position));
        }
    }

    public abstract void bindData(ImageView bannerImg, TextView titleText, T item);

    public abstract void onItemClicked(int position, T item);
}
