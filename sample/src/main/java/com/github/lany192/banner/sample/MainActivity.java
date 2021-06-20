package com.github.lany192.banner.sample;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.github.lany192.banner.BannerView;
import com.github.lany192.banner.IndicatorView;
import com.github.lany192.banner.sample.transformer.GalleryPageTransformer;
import com.github.lany192.banner.sample.transformer.OverlayPageTransformer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BannerView bannerView = findViewById(R.id.banner);
        bannerView.setAdapter(new BannerAdapter());
        bannerView.setPageMargin(DensityUtils.dp2px(50), DensityUtils.dp2px(10));
        bannerView.addPageTransformer(new GalleryPageTransformer());

        BannerView bannerView2 = findViewById(R.id.banner2);
        bannerView2.setAdapter(new BannerAdapter());
        bannerView2.addPageTransformer(new MarginPageTransformer(DensityUtils.dp2px(10)));

        BannerView bannerView3 = findViewById(R.id.banner3);
        bannerView3.setAdapter(new BannerAdapter());
//        bannerView3.addPageTransformer(new MarginPageTransformer(20));
        bannerView3.setIndicator(new IndicatorView(this)
                .setIndicatorColor(Color.GRAY)
                .setIndicatorSelectorColor(Color.RED))
                .setPageMargin(DensityUtils.dp2px(20), DensityUtils.dp2px(10))
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {

                    }
                });
    }
}
