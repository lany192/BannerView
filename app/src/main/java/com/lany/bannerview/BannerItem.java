package com.lany.bannerview;


public class BannerItem {
    private String pic;
    private String title;

    public BannerItem() {
    }

    @Override
    public String toString() {
        return "BannerItem{" +
                "pic='" + pic + '\'' +
                ", title='" + title + '\'' +

                '}';
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
