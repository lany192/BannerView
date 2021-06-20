package com.github.lany192.banner.sample.transformer;

import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class OverlayPageTransformer implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(@NonNull View pageView, float position) {
        if (position <= 0) {
            pageView.setTranslationX(0);
        } else {
            float scaleOffset = 150f;
            float transOffset = 200f;
            float transX = -pageView.getWidth() * position + transOffset * position;
            pageView.setTranslationX(transX);
            float scale = (pageView.getWidth() - scaleOffset * position) / (float) (pageView.getWidth());
            pageView.setScaleX(scale);
            pageView.setScaleY(scale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                pageView.setTranslationZ(-position);
            }
        }
    }
}
