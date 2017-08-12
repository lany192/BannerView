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
        <declare-styleable name="BannerStyle">
            <!--不同界面切换时间-->
            <attr name="banner_delayTime" format="integer" />
            <!--切换过程滑动时间-->
            <attr name="banner_scrollTime" format="integer" />
            <!--是否自动播放-->
            <attr name="banner_isAutoPlay" format="boolean" />
            <!--标题背景颜色-->
            <attr name="banner_titleBackground" format="color|reference" />
            <!--标题颜色-->
            <attr name="banner_titleTextColor" format="color" />
            <!--标题大小-->
            <attr name="banner_titleTextSize" format="dimension" />
            <!--标题高度-->
            <attr name="banner_titleHeight" format="dimension" />
            <!--指示器之间的间隙-->
            <attr name="banner_indicatorMargin" format="dimension" />
            <!--选中的指示器图片-->
            <attr name="banner_indicatorSelectedDrawable" format="reference" />
            <!--未选中的指示器图片-->
            <attr name="banner_indicatorUnselectedDrawable" format="reference" />
            <!--指示器的几种样式-->
            <attr name="banner_indicatorType" format="enum">
                <enum name="not_indicator" value="0" />
                <enum name="circle_indicator" value="1" />
                <enum name="num_indicator" value="2" />
                <enum name="num_indicator_title" value="3" />
                <enum name="circle_indicator_title" value="4" />
                <enum name="circle_indicator_title_inside" value="5" />
            </attr>
            <!--指示器显示位置->
            <attr name="banner_indicatorGravity" format="enum">
                <enum name="top" value="0x30" />
                <enum name="bottom" value="0x50" />
                <enum name="left" value="0x03" />
                <enum name="right" value="0x05" />
                <enum name="center_vertical" value="0x10" />
                <enum name="fill_vertical" value="0x70" />
                <enum name="center_horizontal" value="0x01" />
                <enum name="fill_horizontal" value="0x07" />
                <enum name="center" value="0x11" />
                <enum name="fill" value="0x77" />
                <enum name="clip_vertical" value="0x80" />
                <enum name="clip_horizontal" value="0x08" />
                <enum name="start" value="0x00800003" />
                <enum name="end" value="0x00800005" />
            </attr>
            <attr name="banner_layoutId" format="reference" />
            <!--banner图片的显示方式->
            <attr name="banner_scaleType" format="enum">
                <enum name="matrix" value="0" />
                <enum name="fitXY" value="1" />
                <enum name="fitStart" value="2" />
                <enum name="fitCenter" value="3" />
                <enum name="fitEnd" value="4" />
                <enum name="center" value="5" />
                <enum name="centerCrop" value="6" />
                <enum name="centerInside" value="7" />
            </attr>
        </declare-styleable>
# Demo
[Demo apk](https://github.com/lany192/BannerView/raw/master/preview/app-release.apk)
# Preview
![image](https://github.com/lany192/BannerView/raw/master/preview/pic.png)

