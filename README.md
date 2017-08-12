# BannerView
This is an android banner view


这是一个轻量简洁的自定义banner控件

# Gradle
       compile 'com.lany:BannerView:2.0.3'
# Usage
## xml layout
        <com.lany.view.BannerView
            android:id="@+id/banner_view"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:layout_marginTop="8dp"
            app:banner_delayTime="5000"
            app:banner_indicatorGravity="center"
            app:banner_indicatorMargin="4dp"
            app:banner_indicatorSelectedDrawable="@drawable/selected"
            app:banner_indicatorType="circle_indicator_title_inside"
            app:banner_indicatorUnselectedDrawable="@drawable/unselected"
            app:banner_isAutoPlay="true"
            app:banner_scaleType="fitXY"
            app:banner_scrollTime="1000"
            app:banner_titleBackground="#50000000"
            app:banner_titleTextColor="#ffffff"
            app:banner_titleTextSize="12sp" />
            
         or default settings:
         
         <com.lany.view.BannerView
             android:id="@+id/banner_view"
             android:layout_width="match_parent"
             android:layout_height="180dp"
             android:layout_marginTop="8dp"/>
 ## code         
            
        BannerView bannerView = (BannerView) findViewById(R.id.banner_view);    
        bannerView.setAdapter(new BannerAdapter<BannerItem>(items) {
            @Override
            public void bindImage(ImageView imageView, BannerItem item) {
                Glide.with(MainActivity.this)
                        .load(item.getPic())
                        .placeholder(R.drawable.pic)
                        .error(R.drawable.pic)
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
## attribute 
|Attributes|format|describe
|---|---|---|
|banner_delayTime| integer|不同界面切换时间
|banner_scrollTime| integer|切换过程滑动时间
|banner_isAutoPlay| boolean|是否自动轮播，默认true
|banner_titleBackground| color|reference|标题背景颜色
|banner_titleTextColor| color|reference|标题颜色
|banner_titleTextSize| dimension|标题字体大小
|banner_titleHeight| dimension|标题栏高度
|banner_indicatorMargin| dimension|指示器之间的间距
|banner_indicatorSelectedDrawable| reference|选中的指示器图片
|banner_indicatorUnselectedDrawable| reference|未选中的指示器图片
|banner_scaleType| enum |banner图片的显示方式
|banner_indicatorType| enum |指示器的几种样式
|banner_indicatorGravity| enum |指示器显示位置
# Demo
[Demo apk](https://github.com/lany192/BannerView/raw/master/preview/app-release.apk)
# Preview
![image](https://github.com/lany192/BannerView/raw/master/preview/pic.png)

