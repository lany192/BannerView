package com.lany.bannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lany.view.BannerAdapter;
import com.lany.view.BannerView;

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
        //items2.addAll(getItems());


        bannerView2.setAdapter(new BannerAdapter<BannerItem>(items2) {

            @Override
            public void bindItem(ImageView bannerImg, TextView titleText, BannerItem item) {
                titleText.setText(item.getTitle());
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(bannerImg);
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + item, Toast.LENGTH_SHORT).show();
            }
        });

        List<BannerItem> items3 = new ArrayList<>();
        BannerItem item1 = new BannerItem();
        item1.setPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489902136826&di=b5ff524bb0fcaa460e6a4c398b48e1e4&imgtype=0&src=http%3A%2F%2Fimg3.91.com%2Fuploads%2Fallimg%2F130428%2F32-13042Q63239.jpg");
        item1.setTitle("title7");
        items3.add(item1);
        items3.addAll(items);

        bannerView3.setAdapter(new BannerAdapter<BannerItem>(items3) {

            @Override
            public void bindItem(ImageView bannerImg, TextView titleText, BannerItem item) {
                titleText.setText(item.getTitle());
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(bannerImg);
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBanner1() {
        bannerView.setAdapter(new BannerAdapter<BannerItem>(items) {

            @Override
            public void bindItem(ImageView bannerImg, TextView titleText, BannerItem item) {
                //title.setText(item.getTips());
                Glide.with(MainActivity.this).load(item.getPic()).placeholder(R.drawable.pic).error(R.drawable.pic).into(bannerImg);
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + item, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
