# BannerView
This is an android banner view


这是一个轻量简洁的自定义banner控件

# Gradle
       compile 'com.lany:BannerView:2.0.0'
# Usage
       <com.lany.view.BannerView
            android:id="@+id/banner_view"
            android:layout_width="match_parent"
            android:layout_height="180dp"/>
            
        bannerView.setImages(items).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                BannerItem item = (BannerItem) path;
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }
        });
        bannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        bannerView.start();
# Preview
![image](https://github.com/lany192/BannerView/raw/master/preview/pic1.png)

