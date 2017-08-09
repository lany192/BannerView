package com.lany.bannerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.lany.view.BannerView;
import com.lany.view.ImageLoader;
import com.lany.view.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

public class PageTransformerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    BannerView bannerView;
    List<Class<? extends ViewPager.PageTransformer>> items = new ArrayList<>();

    public void initData() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_transformer);
        initData();
        bannerView = (BannerView) findViewById(R.id.banner);
        ListView listView = (ListView) findViewById(R.id.list);
        String[] data = getResources().getStringArray(R.array.anim);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
        listView.setOnItemClickListener(this);


        bannerView.setImages(DataUtils.getItems()).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                BannerItem item = (BannerItem) path;
                Glide.with(PageTransformerActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }
        });
        bannerView.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(PageTransformerActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView.setAnimation(Transformer.Default);
        bannerView.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bannerView.setAnimation(items.get(position));
    }
}