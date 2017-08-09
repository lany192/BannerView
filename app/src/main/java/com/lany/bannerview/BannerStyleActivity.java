package com.lany.bannerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lany.view.BannerStyle;
import com.lany.view.BannerView;
import com.lany.view.ImageLoader;

public class BannerStyleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    BannerView bannerView;
    Spinner spinnerStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_style);
        bannerView = (BannerView) findViewById(R.id.banner);
        spinnerStyle = (Spinner) findViewById(R.id.spinnerStyle);
        spinnerStyle.setOnItemSelectedListener(this);
        bannerView.setImages(DataUtils.getItems()).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                BannerItem item = (BannerItem) path;
                Glide.with(BannerStyleActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }
        });
        bannerView.setOnItemClickListener(new BannerView.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(BannerStyleActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView.setAnimation(Transformer.Default);
        bannerView.setBannerStyle(BannerStyle.NOT_INDICATOR);
        bannerView.start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                bannerView.updateStyle(BannerStyle.NOT_INDICATOR);
                break;
            case 1:
                bannerView.updateStyle(BannerStyle.CIRCLE_INDICATOR);
                break;
            case 2:
                bannerView.updateStyle(BannerStyle.NUM_INDICATOR);
                break;
            case 3:
                bannerView.updateStyle(BannerStyle.NUM_INDICATOR_TITLE);
                break;
            case 4:
                bannerView.updateStyle(BannerStyle.CIRCLE_INDICATOR_TITLE);
                break;
            case 5:
                bannerView.updateStyle(BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
