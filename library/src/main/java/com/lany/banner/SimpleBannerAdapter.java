package com.lany.banner;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SimpleBannerAdapter<T> extends BannerAdapter<T> {

    public SimpleBannerAdapter(List<T> items) {
        super(items);
    }

    @Override
    public void bindImage(ImageView imageView, T item) {

    }

    @Override
    public void bindTitle(TextView titleText, T item) {

    }

    @Override
    public void onItemClicked(int position, T item) {

    }
}
