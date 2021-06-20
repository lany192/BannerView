package com.github.lany192.banner.sample;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public class BannerAdapter extends BaseQuickAdapter<BannerItem, BaseViewHolder> {

    public BannerAdapter() {
        super(R.layout.item_banner_view, DataUtils.getItems());
    }

    @Override
    protected void convert(BaseViewHolder holder, BannerItem item) {
        ImageView imageView =holder.getView(R.id.image_view);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.pic)
                .error(R.drawable.pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(item.getPic())
                .into(imageView);
    }
}
