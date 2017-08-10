# BannerView
This is an android banner view


这是一个轻量简洁的自定义banner控件

# Gradle
       compile 'com.lany:BannerView:2.0.1'
# Usage
       <com.lany.view.BannerView
            android:id="@+id/banner_view"
            android:layout_width="match_parent"
            android:layout_height="180dp"/>
            
        bannerView.setAdapter(new BannerAdapter<BannerItem>(items) {
            @Override
            public void bindItem(ImageView imageView, TextView title, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
                        .into(imageView);
            }

            @Override
            public void onItemClicked(int position, BannerItem item) {
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
# Preview
![image](https://github.com/lany192/BannerView/raw/master/preview/pic.png)

