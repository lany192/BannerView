# BannerView
这是一个轻量简洁的自定义banner控件。欢迎大家提出意见或建议

BannerView在控件可见时自动播放，不可见时停止播放，不需要额外设置界面切换时的开始播放和暂停播放。

# Gradle
       compile 'com.lany:BannerView:2.1.0'
# Usage
## xml layout
    <com.lany.banner.BannerView
        android:id="@+id/banner_view"
        android:layout_width="match_parent"
        android:layout_height="180dp"
        app:banner_delayTime="5000"
        app:banner_indicatorGravity="center"
        app:banner_indicatorMargin="4dp"
        app:banner_indicatorSelected="@drawable/selected"
        app:banner_indicatorType="circle_indicator_title_inside"
        app:banner_indicatorUnselected="@drawable/unselected"
        app:banner_isAutoPlay="true"
        app:banner_scaleType="fitXY"
        app:banner_scrollTime="1000"
        app:banner_titleBackground="#50000000"
        app:banner_titleTextColor="#ffffff"
        app:banner_titleTextSize="12sp" />

##### 也可以使用默认配置:
         
     <com.lany.banner.BannerView
         android:id="@+id/banner_view"
         android:layout_width="match_parent"
         android:layout_height="180dp"/>
 ## code         
            
    BannerView bannerView = (BannerView) findViewById(R.id.banner_view);    
    //对banner的数据源list的数据结构不做要求，BannerItem可替换成你自己的数据结构
    bannerView.setAdapter(new BannerAdapter<BannerItem>(items) {
        @Override
        public void bindImage(ImageView imageView, BannerItem item) {
            //这里处理图片的数据绑定，可以使用任意一种图片加载框架
            Glide.with(MainActivity.this)
                    .load(item.getPic())
                    .placeholder(R.drawable.pic)
                    .error(R.drawable.pic)
                    .into(imageView);
        }

        @Override
        public void bindTitle(TextView titleText, BannerItem item) {
            //这里处理标题内容的绑定。如果不需要显示标题，可不做处理
            titleText.setText("" + item.getTitle());
        }

        @Override
        public void onItemClicked(int position, BannerItem item) {
            //如果需要处理banner的点击事件，这里处理
            Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
        }
    });

## attribute

|Attributes|format|describe
|---|---|---|
|banner_delayTime| integer|不同界面切换时间
|banner_scrollTime| integer|切换过程滑动时间
|banner_isAutoPlay| boolean|是否自动轮播，默认true
|banner_titleBackground| color|标题背景颜色
|banner_titleTextColor| color|标题颜色
|banner_titleTextSize| dimension|标题字体大小
|banner_titleHeight| dimension|标题栏高度
|banner_indicatorMargin| dimension|指示器之间的间距
|banner_indicatorSelected| reference|选中的指示器图片
|banner_indicatorUnselected| reference|未选中的指示器图片
|banner_scaleType| enum |banner图片的显示方式
|banner_indicatorType| enum |指示器的几种样式
|banner_indicatorGravity| enum |指示器显示位置
# Demo
[点击下载 download Demo apk](https://github.com/lany192/BannerView/raw/master/preview/app-release.apk)
# Preview
![image](https://github.com/lany192/BannerView/raw/master/preview/pic.png)

## License


Copyright 2017 Y.G. Lan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

