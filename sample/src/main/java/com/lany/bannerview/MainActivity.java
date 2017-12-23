package com.lany.bannerview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lany.banner.BannerAdapter;
import com.lany.banner.BannerStyle;
import com.lany.banner.BannerView;
import com.lany.bannerview.transformer.AccordionTransformer;
import com.lany.bannerview.transformer.BackgroundToForegroundTransformer;
import com.lany.bannerview.transformer.CubeInTransformer;
import com.lany.bannerview.transformer.CubeOutTransformer;
import com.lany.bannerview.transformer.DefaultTransformer;
import com.lany.bannerview.transformer.DepthPageTransformer;
import com.lany.bannerview.transformer.FlipHorizontalTransformer;
import com.lany.bannerview.transformer.FlipVerticalTransformer;
import com.lany.bannerview.transformer.ForegroundToBackgroundTransformer;
import com.lany.bannerview.transformer.RotateDownTransformer;
import com.lany.bannerview.transformer.RotateUpTransformer;
import com.lany.bannerview.transformer.ScaleInOutTransformer;
import com.lany.bannerview.transformer.StackTransformer;
import com.lany.bannerview.transformer.TabletTransformer;
import com.lany.bannerview.transformer.ZoomInTransformer;
import com.lany.bannerview.transformer.ZoomOutSlideTransformer;
import com.lany.bannerview.transformer.ZoomOutTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<BannerItem> items1 = new ArrayList<>();
    private BannerView bannerView1;
    private BannerView bannerView;
    private List<Class<? extends ViewPager.PageTransformer>> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        bannerView = (BannerView) findViewById(R.id.banner);
        Spinner spinnerStyle = (Spinner) findViewById(R.id.spinnerStyle);
        Spinner spinnerStyle2 = (Spinner) findViewById(R.id.spinnerTransformer);
        bannerView1 = (BannerView) findViewById(R.id.banner_view_1);
        BannerView bannerView2 = (BannerView) findViewById(R.id.banner_view_2);
        BannerView bannerView3 = (BannerView) findViewById(R.id.banner_view_3);
        BannerView bannerView4 = (BannerView) findViewById(R.id.banner_view_4);
        BannerView bannerView5 = (BannerView) findViewById(R.id.banner_view_5);
        BannerView bannerView6 = (BannerView) findViewById(R.id.banner_view_6);


        spinnerStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        bannerView.resetStyle(BannerStyle.NOT_INDICATOR);
                        break;
                    case 1:
                        bannerView.resetStyle(BannerStyle.CIRCLE_INDICATOR);
                        break;
                    case 2:
                        bannerView.resetStyle(BannerStyle.NUM_INDICATOR);
                        break;
                    case 3:
                        bannerView.resetStyle(BannerStyle.NUM_INDICATOR_TITLE);
                        break;
                    case 4:
                        bannerView.resetStyle(BannerStyle.CIRCLE_INDICATOR_TITLE);
                        break;
                    case 5:
                        bannerView.resetStyle(BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerStyle2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bannerView.setAnimation(items.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bannerView.setAnimation(Transformer.Default).setAdapter(new BannerAdapter<BannerItem>(DataUtils.getItems()) {
            @Override
            public void bindImage(ImageView imageView, BannerItem item) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(MainActivity.this)
                        .setDefaultRequestOptions(options)
                        .load(item.getPic())
                        .into(imageView);
            }

            @Override
            public void bindTitle(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.my_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerItem banner = new BannerItem();
                banner.setPic("http://f.hiphotos.baidu.com/image/pic/item/d1a20cf431adcbef43977ecca5af2edda2cc9f5c.jpg");
                banner.setTitle("title6");
                items1.add(banner);
                initBanner1();
            }
        });
        items1.addAll(DataUtils.getItems());
        initBanner1();


        List<BannerItem> items2 = new ArrayList<>();
        BannerItem item = new BannerItem();
        item.setPic("http://f.hiphotos.baidu.com/image/pic/item/4ec2d5628535e5dd623069aa7cc6a7efce1b62a3.jpg");
        item.setTitle("title6");
        items2.add(item);
        items2.addAll(DataUtils.getItems());
        bannerView2.setAdapter(new BannerAdapter<BannerItem>(items2) {

            @Override
            public void bindImage(ImageView imageView, BannerItem item) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(MainActivity.this)
                        .setDefaultRequestOptions(options)
                        .load(item.getPic())
                        .into(imageView);
            }

            @Override
            public void bindTitle(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        List<BannerItem> items3 = new ArrayList<>();
        BannerItem item1 = new BannerItem();
        item1.setPic("http://a.hiphotos.baidu.com/image/pic/item/a9d3fd1f4134970a300589e59ccad1c8a6865d81.jpg");
        item1.setTitle("title7");
        items3.add(item1);
        items3.addAll(DataUtils.getItems());

        bannerView3.setAdapter(new BannerAdapter<BannerItem>(items3) {
            @Override
            public void bindImage(ImageView imageView, BannerItem item) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(MainActivity.this)
                        .setDefaultRequestOptions(options)
                        .load(item.getPic())
                        .into(imageView);
            }

            @Override
            public void bindTitle(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        bannerView4.setAdapter(new BannerAdapter<BannerItem>(items3) {
            @Override
            public void bindImage(ImageView imageView, BannerItem item) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(MainActivity.this)
                        .setDefaultRequestOptions(options)
                        .load(item.getPic())
                        .into(imageView);
            }

            @Override
            public void bindTitle(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView5.setAdapter(new BannerAdapter<BannerItem>(items3) {
            @Override
            public void bindImage(ImageView imageView, BannerItem item) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(MainActivity.this)
                        .setDefaultRequestOptions(options)
                        .load(item.getPic())
                        .into(imageView);
            }

            @Override
            public void bindTitle(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView6.setAdapter(new BannerAdapter<BannerItem>(items3) {
            @Override
            public void bindImage(ImageView imageView, BannerItem item) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(MainActivity.this)
                        .setDefaultRequestOptions(options)
                        .load(item.getPic())
                        .into(imageView);
            }

            @Override
            public void bindTitle(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initData() {
        items.clear();
        items.add(DefaultTransformer.class);
        items.add(AccordionTransformer.class);
        items.add(BackgroundToForegroundTransformer.class);
        items.add(ForegroundToBackgroundTransformer.class);
        items.add(CubeInTransformer.class);
        items.add(CubeOutTransformer.class);
        items.add(DepthPageTransformer.class);
        items.add(FlipHorizontalTransformer.class);
        items.add(FlipVerticalTransformer.class);
        items.add(RotateDownTransformer.class);
        items.add(RotateUpTransformer.class);
        items.add(ScaleInOutTransformer.class);
        items.add(StackTransformer.class);
        items.add(TabletTransformer.class);
        items.add(ZoomInTransformer.class);
        items.add(ZoomOutTransformer.class);
        items.add(ZoomOutSlideTransformer.class);
    }

    private void initBanner1() {
        bannerView1.setAnimation(Transformer.FlipHorizontal)
                .setAdapter(new BannerAdapter<BannerItem>(items1) {
                    @Override
                    public void bindImage(ImageView imageView, BannerItem item) {
                        RequestOptions options = new RequestOptions()
                                .fitCenter()
                                .dontAnimate()
                                .placeholder(R.drawable.pic)
                                .error(R.drawable.pic)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                        Glide.with(MainActivity.this)
                                .setDefaultRequestOptions(options)
                                .load(item.getPic())
                                .into(imageView);
                    }

                    @Override
                    public void bindTitle(TextView titleText, BannerItem item) {
                        titleText.setText("" + item.getTitle());
                        Log.i("TAG", "bindTitle: BannerItem==" + item);
                    }

                    @Override
                    public void onItemClicked(int position, BannerItem item) {
                        Log.i("TAG", "onItemClicked: BannerItem==" + item);
                        Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
