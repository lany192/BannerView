package com.lany.bannerview;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    private static List<BannerItem> items = new ArrayList<>();

    public static List<BannerItem> getItems() {
        items.clear();
        BannerItem banner = new BannerItem();
        banner.setPic("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2167376170,2737558790&fm=11&gp=0.jpg");
        banner.setTitle("title1");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://www.1tong.com/uploads/wallpaper/plants/281-5-730x456.jpg");
        banner.setTitle("title2");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://www.1tong.com/uploads/wallpaper/plants/184-4-1024x768.jpg");
        banner.setTitle("title3");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://www.1tong.com/uploads/wallpaper/plants/184-5-1024x768.jpg");
        banner.setTitle("title4");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://pic17.nipic.com/20111122/6759425_152002413138_2.jpg");
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
