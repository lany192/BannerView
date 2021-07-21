package com.github.lany192.banner.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.github.lany192.banner.BannerView;
import com.github.lany192.banner.IndicatorView;
import com.github.lany192.banner.sample.transformer.GalleryPageTransformer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BannerView bannerView = findViewById(R.id.banner);
        bannerView.setPageMargin(DensityUtils.dp2px(50), DensityUtils.dp2px(10));
        bannerView.addPageTransformer(new GalleryPageTransformer());
        bannerView.setAdapter(new BannerAdapter());

        BannerView bannerView2 = findViewById(R.id.banner2);
        bannerView2.addPageTransformer(new MarginPageTransformer(DensityUtils.dp2px(10)));
        bannerView2.setAdapter(new BannerAdapter());

        IndicatorView indicatorView = new IndicatorView(this)
                .setIndicatorRatio(1f)
                .setIndicatorRadius(2f)
                .setIndicatorSelectedRatio(3)
                .setIndicatorSelectedRadius(2f)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BIG_CIRCLE)
                .setIndicatorColor(Color.GRAY)
                .setIndicatorSelectorColor(Color.WHITE);
        BannerView bannerView3 = findViewById(R.id.banner3);
        bannerView3.setAutoPlay(false)
                .setIndicator(indicatorView)
                .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
                .setPagerScrollDuration(1000)
                .setPageMargin(DensityUtils.dp2px(20), DensityUtils.dp2px(10))
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Log.e("aa", "onPageSelected position " + position);
                    }
                });
        //setAdapter要放在最后面
        bannerView3.setAdapter(new BannerAdapter());
    }
}
