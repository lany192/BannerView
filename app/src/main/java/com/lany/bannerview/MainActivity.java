package com.lany.bannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lany.view.BannerAdapter;
import com.lany.view.BannerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BannerView bannerView = (BannerView) findViewById(R.id.banner_view);
        BannerView bannerView2 = (BannerView) findViewById(R.id.banner_view_2);
        BannerView bannerView3 = (BannerView) findViewById(R.id.banner_view_3);

        bannerView.setAdapter(new BannerAdapter<BannerItem>(getItems()) {

            @Override
            public void bindData(ImageView bannerImg, TextView titleText, BannerItem item) {
                //title.setText(item.getTips());
                Glide.with(MainActivity.this)
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(bannerImg);
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });


        List<BannerItem> items2 = new ArrayList<>();
        BannerItem item = new BannerItem();
        item.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489902136826&di=62cf54ead218933744db70780d3ae8b5&imgtype=0&src=http%3A%2F%2Fhomemade.keliren.cn%2Ftuku%2Fa%2F20160406%2F570461f141575.jpg");
        item.setTips("title6");
        items2.add(item);
        //items2.addAll(getItems());


        bannerView2.setAdapter(new BannerAdapter<BannerItem>(items2) {

            @Override
            public void bindData(ImageView bannerImg, TextView titleText, BannerItem item) {
                titleText.setText(item.getTips());
                Glide.with(MainActivity.this)
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(bannerImg);
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        List<BannerItem> items3 = new ArrayList<>();
        BannerItem item1 = new BannerItem();
        item1.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489902136826&di=b5ff524bb0fcaa460e6a4c398b48e1e4&imgtype=0&src=http%3A%2F%2Fimg3.91.com%2Fuploads%2Fallimg%2F130428%2F32-13042Q63239.jpg");
        item1.setTips("title7");
        items3.add(item1);
        items3.addAll(getItems());

        bannerView3.setAdapter(new BannerAdapter<BannerItem>(items3) {

            @Override
            public void bindData(ImageView bannerImg, TextView titleText, BannerItem item) {
                titleText.setText(item.getTips());
                Glide.with(MainActivity.this)
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(bannerImg);
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<BannerItem> getItems() {
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

        return items;
    }
}
