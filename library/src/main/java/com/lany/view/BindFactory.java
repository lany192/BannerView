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

    public void bind(ImageView imageView, TextView tv, int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            bindItem(imageView, tv, mItems.get(position));
        }
    }

    public void selectClicked(int position) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size() && position >= 0) {
            onItemClicked(position, mItems.get(position));
        }
    }

    public abstract void bindItem(ImageView imageView, TextView title, T item);

    public abstract void onItemClicked(int position, T item);
}