package com.lany.bannerview;

import android.content.Context;
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
    List<BannerItem> items = new ArrayList<>();
    BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.banner_view);
        BannerView bannerView2 = (BannerView) findViewById(R.id.banner_view_2);
        BannerView bannerView3 = (BannerView) findViewById(R.id.banner_view_3);
        findViewById(R.id.my_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerItem banner = new BannerItem();
                banner.setPic("http://imgsrc.baidu.com/imgad/pic/item/b03533fa828ba61e5e6d4c0d4b34970a304e5915.jpg");
                banner.setTitle("title6");
                items.add(banner);
                initBanner1();
            }
        });
        findViewById(R.id.my_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() > 0) {
                    items.remove(0);
                    initBanner1();
                }
            }
        });

        BannerItem banner = new BannerItem();
        banner.setPic("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2167376170,2737558790&fm=11&gp=0.jpg");
        banner.setTitle("title1");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://www.1tong.com/uploads/wallpaper/plants/281-5-730x456.jpg");
        banner.setTitle("title2");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://www.1tong.com/uploads/wallpaper/plants/184-4-1024x768.jpg");
        banner.setTitle("title3");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://www.1tong.com/uploads/wallpaper/plants/184-5-1024x768.jpg");
        banner.setTitle("title4");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://pic17.nipic.com/20111122/6759425_152002413138_2.jpg");
        banner.setTitle("title5");
        items.add(banner);

        initBanner1();


        List<BannerItem> items2 = new ArrayList<>();
        BannerItem item = new BannerItem();
        item.setPic("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg");
        item.setTitle("title6");
        items2.add(item);
        items2.addAll(items);
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
        items3.addAll(items);


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
        bannerView.setImages(items).setImageLoader(new ImageLoader() {
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
