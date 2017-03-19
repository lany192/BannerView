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

    public void setImageViewSource(ImageView bannerImg, int position) {
        bindImage(bannerImg, mItems.get(position));
    }

    public void selectTips(TextView tv, int position) {
        if (mItems != null && mItems.size() > 0) {
            bindTitle(tv, mItems.get(position));
        }
    }

    public abstract void bindTitle(TextView titleText, T item);

    public abstract void bindImage(ImageView bannerImg, T item);


}
