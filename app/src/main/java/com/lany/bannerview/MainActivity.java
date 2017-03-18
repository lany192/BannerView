package com.lany.bannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.lany.view.BannerAdapter;
import com.lany.view.BannerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BannerView bannerView = (BannerView) findViewById(R.id.id_banner);

        List<BannerItem> items = new ArrayList<>();

        BannerItem banner = new BannerItem();
        banner.setImageUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2167376170,2737558790&fm=11&gp=0.jpg");
        banner.setTips("title1");
        items.add(banner);

        banner = new BannerItem();
        banner.setImageUrl("http://www.1tong.com/uploads/wallpaper/plants/281-5-730x456.jpg");
        banner.setTips("title2");
        items.add(banner);

        banner = new BannerItem();
        banner.setImageUrl("http://www.1tong.com/uploads/wallpaper/plants/184-4-1024x768.jpg");
        banner.setTips("title3");
        items.add(banner);

        banner = new BannerItem();
        banner.setImageUrl("http://www.1tong.com/uploads/wallpaper/plants/184-5-1024x768.jpg");
        banner.setTips("title4");
        items.add(banner);

        banner = new BannerItem();
        banner.setImageUrl("http://www.1tong.com/uploads/wallpaper/plants/281-5-730x456.jpg");
        banner.setTips("title5");
        items.add(banner);

        bannerView.setAdapter(new BannerAdapter<BannerItem>(items) {

            @Override
            protected void bindTitle(TextView title, BannerItem item) {
                //title.setText(item.getTips());
            }

            @Override
            public void bindImage(ImageView bannerImg, BannerItem item) {
                ImageHelper.getInstance().show(item.getImageUrl(), bannerImg);
            }
        });
    }
}
