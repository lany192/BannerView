package com.github.lany192.banner.sample;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    private static final List<BannerItem> items = new ArrayList<>();

    public static List<BannerItem> getItems() {
        items.clear();
        BannerItem banner = new BannerItem();
        banner.setPic("https://t7.baidu.com/it/u=26855683,918103470&fm=193&f=GIF");
        banner.setTitle("title1");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("https://t7.baidu.com/it/u=1339544566,2936337339&fm=193&f=GIF");
        banner.setTitle("title2");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01d56b5542d8bc0000019ae98da289.jpg%401280w_1l_2o_100sh.png&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626355491&t=fad0899b7f76e7b2bd50ec66f6b92fa2");
        banner.setTitle("title3");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.616pic.com%2Fbg_w1180%2F00%2F04%2F86%2Ffu7qDUVQnP.jpg&refer=http%3A%2F%2Fpic.616pic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626355491&t=115905436c310c2589b7f870cd6f5830");
        banner.setTitle("title4");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fku.90sjimg.com%2Fback_pic%2F05%2F14%2F44%2F5259a672b99182d.jpg&refer=http%3A%2F%2Fku.90sjimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626355491&t=35d7b46fcaf0e96167edda26c506978e");
        banner.setTitle("title5");
        items.add(banner);

        return items;
    }

    public static void addItem(BannerItem item) {
        items.add(item);
    }

    public static void removeItem(BannerItem item) {
        items.remove(item);
    }

    public static void removeItem(int index) {
        items.remove(index);
    }
}
