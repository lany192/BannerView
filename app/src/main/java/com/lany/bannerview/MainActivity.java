package com.lany.bannerview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.lany.bannerview.transformer.ZoomOutTranformer;
import com.lany.view.BannerStyle;
import com.lany.view.BannerView;
import com.lany.view.BindFactory;

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
        bannerView.setAnimation(Transformer.Default).setBindFactory(new BindFactory<BannerItem>(DataUtils.getItems()) {
            @Override
            public void bindImageView(ImageView imageView, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }

            @Override
            public void bindTitleText(TextView titleText, BannerItem item) {
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
                banner.setPic("http://imgsrc.baidu.com/imgad/pic/item/b03533fa828ba61e5e6d4c0d4b34970a304e5915.jpg");
                banner.setTitle("title6");
                items1.add(banner);
                initBanner1();
            }
        });
        items1.addAll(DataUtils.getItems());
        initBanner1();


        List<BannerItem> items2 = new ArrayList<>();
        BannerItem item = new BannerItem();
        item.setPic("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg");
        item.setTitle("title6");
        items2.add(item);
        items2.addAll(DataUtils.getItems());
        bannerView2.setBindFactory(new BindFactory<BannerItem>(items2) {

            @Override
            public void bindImageView(ImageView imageView, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }

            @Override
            public void bindTitleText(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        List<BannerItem> items3 = new ArrayList<>();
        BannerItem item1 = new BannerItem();
        item1.setPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489902136826&di=b5ff524bb0fcaa460e6a4c398b48e1e4&imgtype=0&src=http%3A%2F%2Fimg3.91.com%2Fuploads%2Fallimg%2F130428%2F32-13042Q63239.jpg");
        item1.setTitle("title7");
        items3.add(item1);
        items3.addAll(DataUtils.getItems());

        bannerView3.setBindFactory(new BindFactory<BannerItem>(items3) {
            @Override
            public void bindImageView(ImageView imageView, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }

            @Override
            public void bindTitleText(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        bannerView4.setBindFactory(new BindFactory<BannerItem>(items3) {
            @Override
            public void bindImageView(ImageView imageView, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }

            @Override
            public void bindTitleText(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView5.setBindFactory(new BindFactory<BannerItem>(items3) {
            @Override
            public void bindImageView(ImageView imageView, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }

            @Override
            public void bindTitleText(TextView titleText, BannerItem item) {
                titleText.setText("" + item.getTitle());
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView6.setBindFactory(new BindFactory<BannerItem>(items3) {
            @Override
            public void bindImageView(ImageView imageView, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }

            @Override
            public void bindTitleText(TextView titleText, BannerItem item) {
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
        items.add(ZoomOutTranformer.class);
        items.add(ZoomOutSlideTransformer.class);
    }

    private void initBanner1() {
        bannerView1.setAnimation(Transformer.FlipHorizontal)
                .setBindFactory(new BindFactory<BannerItem>(items1) {
                    @Override
                    public void bindImageView(ImageView imageView, BannerItem item) {
                        Glide.with(MainActivity.this)
                                .load(item.getPic())
                                .placeholder(R.drawable.pic)
                                .error(R.drawable.pic)
                                .into(imageView);
                    }

                    @Override
                    public void bindTitleText(TextView titleText, BannerItem item) {
                        titleText.setText("" + item.getTitle());
                    }

                    @Override
                    public void onItemClicked(int position, BannerItem item) {
                        Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
