package com.lany.view;


import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class BindFactory<T> {
    private List<T> mItems = new ArrayList<>();

    public List<T> getItems() {
        return mItems;
    }

    public T get(int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            return mItems.get(position);
        } else {
            return null;
        }
    }

    public int size() {
        return mItems.size();
    }

    public BindFactory(List<T> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    public void bind(ImageView imageView, int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            bindImageView(imageView, mItems.get(position));
        }
    }

    public void setTitleData(TextView titleText, int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            bindTitleText(titleText, mItems.get(position));
        }
    }

    public void selectClicked(int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            onItemClicked(position, mItems.get(position));
        }
    }

    public abstract void bindImageView(ImageView imageView, T item);

    public abstract void bindTitleText(TextView titleText, T item);

    public abstract void onItemClicked(int position, T item);
}