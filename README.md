# BannerView
This is an android banner view


这是一个轻量简洁的自定义banner控件


compile 'com.lany:BannerView:1.1.2'

       <com.lany.view.BannerView
            android:id="@+id/banner_view"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:layout_marginTop="8dp"
            app:bv_indicator="@drawable/banner_indicator_oval"
            app:bv_indicator_container_bg="@android:color/transparent"
            app:bv_indicator_gravity="center_horizontal"
            app:bv_indicator_left_right_margin="2dp"
            app:bv_indicator_padding="10dp"
            app:bv_indicator_top_bottom_margin="6dp"
            app:bv_page_change_duration="300"
            app:bv_play_interval="3"
            app:bv_title_text_color="#FFFFFF"
            app:bv_title_text_size="12sp" />
            
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

![image](https://github.com/lany192/BannerView/raw/master/preview/preview.png)


