package com.lany.bannerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图片加载工具
 */
public class ImageHelper {
    private volatile static ImageHelper instance;

    public static ImageHelper getInstance() {
        if (instance == null) {
            synchronized (ImageHelper.class) {
                if (instance == null) {
                    instance = new ImageHelper();
                }
            }
        }
        return instance;
    }

    private ImageHelper() {
        Context context = MyApp.mContext;
        int maxImageWidthForDiskCache = context.getResources().getDisplayMetrics().widthPixels;
        int maxImageHeightForDiskCache = context.getResources().getDisplayMetrics().heightPixels;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memoryCacheSize = maxMemory / 6;
        int maxWidth = maxImageWidthForDiskCache / 2;
        int maxHeight = maxImageHeightForDiskCache / 2;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(maxWidth, maxHeight) // default = device screen dimensions 内存缓存文件的最大长宽
                .diskCacheExtraOptions(maxWidth, maxHeight, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
                .threadPoolSize(5) // default  线程池内加载的数量,默认3
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级，默认3
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(memoryCacheSize)) //可以通过自己的内存缓存实现
                //.memoryCache(new WeakMemoryCache()) // 弱内存缓存(图片容易被回收)
                .memoryCacheSizePercentage(60) // default， 内存缓存的最大值占可用内存的百分比
                //.memoryCacheSize(memoryCacheSize)  // 内存缓存的最大值，有设置memoryCacheSizePercentage就不需要设置这个
                .diskCacheSize(300 * 1024 * 1024) // 100 Mb sd卡(本地)缓存的最大值
                //.diskCacheFileCount(500)  // 可以缓存的文件数量
                //.imageDownloader(new BaseImageDownloader(context)) // default
                //.imageDecoder(new BaseImageDecoder(true)) // default
                //.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                //.writeDebugLogs() // 打印debug log
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //默认使用HASHCODE对UIL进行加密命名
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void clearCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    public void show(String url, ImageView imageView) {
        show(url, imageView, 0, R.drawable.pic, null, true);
    }

    public void showAvatar(String url, ImageView imageView) {
        int radius = imageView.getLayoutParams().height / 2;
        show(url, imageView, radius, R.drawable.pic, null, true);
    }

    public void showCircle(String url, ImageView imageView) {
        int radius = imageView.getLayoutParams().height / 2;
        show(url, imageView, radius, R.drawable.pic, null, true);
    }

    public void show(String url, ImageView imageView, boolean cache) {
        show(url, imageView, 0, R.drawable.pic, null, cache);
    }

    public void show(String url, ImageView imageView, int cornerRadiusPixels) {
        show(url, imageView, cornerRadiusPixels, R.drawable.pic, null, true);
    }

    public void show(String url, ImageView imageView, int cornerRadiusPixels, int defaultResId) {
        show(url, imageView, cornerRadiusPixels, defaultResId, null, true);
    }

    public void show(String url, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(url, listener);
    }

    public void show(String url, ImageView imageView, int cornerRadiusPixels, ImageLoadingListener listener) {
        show(url, imageView, cornerRadiusPixels, R.drawable.pic, listener, true);
    }

    public void show(String url, ImageView imageView, ImageLoadingListener listener) {
        show(url, imageView, 0, R.drawable.pic, listener, true);
    }

    /**
     * 显示图片
     *
     * @param url                图片地址
     * @param imageView          显示控件
     * @param cornerRadiusPixels 圆角大小
     * @param defaultResId       默认图片
     * @param listener           加载监听
     * @param cache              是否缓存
     */
    public void show(String url, ImageView imageView, int cornerRadiusPixels, int defaultResId, ImageLoadingListener listener, boolean cache) {
        if (imageView == null) {
            return;
        }
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = 4;
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultResId)
                .showImageForEmptyUri(defaultResId)
                .showImageOnFail(defaultResId)
                .cacheInMemory(cache)
                .cacheOnDisk(cache)
                .considerExifParams(cache)
                .decodingOptions(option)
                .bitmapConfig(Bitmap.Config.RGB_565);
        if (cornerRadiusPixels > 0) {
            builder.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels));
        } else {
            builder.displayer(new SimpleBitmapDisplayer());
        }
        if (listener != null) {
            ImageLoader.getInstance().displayImage(url, imageView, builder.build(), listener);
        } else {
            ImageLoader.getInstance().displayImage(url, imageView, builder.build());
        }
    }
}