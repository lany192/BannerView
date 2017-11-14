package com.lany.bannerview;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    private static List<BannerItem> items = new ArrayList<>();

    public static List<BannerItem> getItems() {
        items.clear();
        BannerItem banner = new BannerItem();
        banner.setPic("http://h.hiphotos.baidu.com/image/pic/item/b7003af33a87e950128ee8a619385343faf2b45a.jpg");
        banner.setTitle("title1");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://b.hiphotos.baidu.com/image/pic/item/8ad4b31c8701a18b96aede3a942f07082838fe7a.jpg");
        banner.setTitle("title2");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://f.hiphotos.baidu.com/image/pic/item/023b5bb5c9ea15ce2b8ee7c3bf003af33b87b293.jpg");
        banner.setTitle("title3");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://g.hiphotos.baidu.com/image/pic/item/ac6eddc451da81cbeffa4fb85866d016092431a3.jpg");
        banner.setTitle("title4");
        items.add(banner);

        banner = new BannerItem();
        banner.setPic("http://h.hiphotos.baidu.com/image/pic/item/e824b899a9014c08897b336f037b02087af4f4bc.jpg");
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
