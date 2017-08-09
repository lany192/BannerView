package com.lany.bannerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lany.view.BannerView;
import com.lany.view.ImageLoader;
import com.lany.view.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.my_button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PageTransformerActivity.class));
            }
        });

        bannerView = (BannerView) findViewById(R.id.banner_view);
        BannerView bannerView2 = (BannerView) findViewById(R.id.banner_view_2);
        BannerView bannerView3 = (BannerView) findViewById(R.id.banner_view_3);
        findViewById(R.id.my_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerItem banner = new BannerItem();
                banner.setPic("http://imgsrc.baidu.com/imgad/pic/item/b03533fa828ba61e5e6d4c0d4b34970a304e5915.jpg");
                banner.setTitle("title6");
                DataUtils.addItem(banner);
                initBanner1();
            }
        });
        findViewById(R.id.my_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataUtils.getItems().size() > 0) {
                    DataUtils.removeItem(0);
                    initBanner1();
                }
            }
        });

        initBanner1();


        List<BannerItem> items2 = new ArrayList<>();
        BannerItem item = new BannerItem();
        item.setPic("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg");
        item.setTitle("title6");
        items2.add(item);
        items2.addAll(DataUtils.getItems());
        bannerView2.setImages(items2).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                BannerItem item = (BannerItem) path;
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }
        });
        bannerView2.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView2.start();

        List<BannerItem> items3 = new ArrayList<>();
        BannerItem item1 = new BannerItem();
        item1.setPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489902136826&di=b5ff524bb0fcaa460e6a4c398b48e1e4&imgtype=0&src=http%3A%2F%2Fimg3.91.com%2Fuploads%2Fallimg%2F130428%2F32-13042Q63239.jpg");
        item1.setTitle("title7");
        items3.add(item1);
        items3.addAll(DataUtils.getItems());


        bannerView3.setImages(items3).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                BannerItem item = (BannerItem) path;
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }
        });
        bannerView3.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView3.start();
    }

    private void initBanner1() {
        bannerView.setImages(DataUtils.getItems()).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                BannerItem item = (BannerItem) path;
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }
        });
        bannerView.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView.setAnimation(Transformer.FlipHorizontal);
        bannerView.start();
    }
}
