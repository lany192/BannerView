package com.lany.view;


import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class BannerAdapter<T> {
    private List<T> mItems = new ArrayList<>();

    public int size() {
        return mItems.size();
    }

    public BannerAdapter(List<T> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    public void setImage(ImageView imageView, int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            bindImage(imageView, mItems.get(position));
        }
    }

    public void setTitle(TextView titleText, int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            bindTitle(titleText, mItems.get(position));
        }
    }

    public void selectClicked(int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            onItemClicked(position, mItems.get(position));
        }
    }

    public abstract void bindImage(ImageView imageView, T item);

    public abstract void bindTitle(TextView titleText, T item);

    public abstract void onItemClicked(int position, T item);
}