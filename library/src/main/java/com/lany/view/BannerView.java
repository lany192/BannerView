package com.lany.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static android.support.v4.view.ViewPager.PageTransformer;

public class BannerView extends FrameLayout implements OnPageChangeListener {
    private final String TAG = "BannerView";
    private int mIndicatorMargin = 5;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int indicatorSize;
    private int mBannerStyle = BannerStyle.CIRCLE_INDICATOR;
    private int mDelayTime = 3000;//单位毫秒
    private int mScrollTime = 800;//单位毫秒
    private boolean isAutoPlay = true;
    private boolean isScroll = true;
    private int mIndicatorSelectedResId = R.drawable.gray_radius;
    private int mIndicatorUnselectedResId = R.drawable.white_radius;
    private int mLayoutResId = R.layout.banner;
    private int mTitleHeight;
    private int mTitleBackground;
    private int mTitleTextColor;
    private int mTitleTextSize;
    private int count = 0;
    private int currentItem;
    private int mGravity = Gravity.CENTER;
    private int lastPosition = 1;
    private int mScaleType = 1;
    private List<String> mTitles = new ArrayList<>();
    private List imageUrls = new ArrayList<>();
    private List<View> imageViews = new ArrayList<>();
    private List<ImageView> indicatorImages = new ArrayList<>();
    private BannerViewPager mViewPager;
    private TextView bannerTitle, numIndicatorInside, numIndicator;
    private LinearLayout indicator, indicatorInside, titleView;
    private ImageLoaderInterface mImageLoader;
    private BannerPagerAdapter adapter;
    private OnPageChangeListener mOnPageChangeListener;
    private BannerScroller mScroller;
    private OnItemClickListener listener;

    private WeakHandler mHandler = new WeakHandler();

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        indicatorSize = context.getResources().getDisplayMetrics().widthPixels / 80;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        imageViews.clear();
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);
        mViewPager = (BannerViewPager) view.findViewById(R.id.bannerViewPager);
        titleView = (LinearLayout) view.findViewById(R.id.titleView);
        indicator = (LinearLayout) view.findViewById(R.id.circleIndicator);
        indicatorInside = (LinearLayout) view.findViewById(R.id.indicatorInside);
        bannerTitle = (TextView) view.findViewById(R.id.bannerTitle);
        numIndicator = (TextView) view.findViewById(R.id.numIndicator);
        numIndicatorInside = (TextView) view.findViewById(R.id.numIndicatorInside);
        initViewPagerScroll();
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerStyle);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.BannerStyle_banner_indicatorWidth, indicatorSize);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.BannerStyle_banner_indicatorHeight, indicatorSize);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.BannerStyle_banner_indicatorMargin, 5);
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.BannerStyle_banner_indicatorSelectedDrawable, R.drawable.gray_radius);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.BannerStyle_banner_indicatorUnselectedDrawable, R.drawable.white_radius);
        mScaleType = typedArray.getInt(R.styleable.BannerStyle_banner_scaleType, mScaleType);
        mDelayTime = typedArray.getInt(R.styleable.BannerStyle_banner_delayTime, 3000);
        mScrollTime = typedArray.getInt(R.styleable.BannerStyle_banner_scrollTime, 800);
        isAutoPlay = typedArray.getBoolean(R.styleable.BannerStyle_banner_isAutoPlay, true);
        mTitleBackground = typedArray.getColor(R.styleable.BannerStyle_banner_titleBackground, -1);
        mTitleHeight = typedArray.getDimensionPixelSize(R.styleable.BannerStyle_banner_titleHeight, -1);
        mTitleTextColor = typedArray.getColor(R.styleable.BannerStyle_banner_titleTextColor, -1);
        mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.BannerStyle_banner_titleTextSize, -1);
        mLayoutResId = typedArray.getResourceId(R.styleable.BannerStyle_banner_layoutId, mLayoutResId);
        mGravity = typedArray.getInt(R.styleable.BannerStyle_banner_indicatorGravity, Gravity.CENTER);

        mBannerStyle = typedArray.getInt(R.styleable.BannerStyle_banner_indicatorType, BannerStyle.CIRCLE_INDICATOR);
        typedArray.recycle();
    }

    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(mViewPager.getContext());
            mScroller.setDuration(mScrollTime);
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public BannerView isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    public BannerView setImageLoader(ImageLoaderInterface mImageLoader) {
        this.mImageLoader = mImageLoader;
        return this;
    }

    public BannerView setDelayTime(int mDelayTime) {
        this.mDelayTime = mDelayTime;
        return this;
    }

    public BannerView setIndicatorGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.START;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.TOP;
            }
            mGravity = gravity;
        }
        return this;
    }

    public BannerView setAnimation(Class<? extends PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(TAG, "Please set the PageTransformer class");
        }
        return this;
    }

    public BannerView setOffscreenPageLimit(int limit) {
        if (mViewPager != null) {
            mViewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }


    public BannerView setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        mViewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    public BannerView setTitles(List<String> mTitles) {
        this.mTitles = mTitles;
        return this;
    }

    public BannerView setBannerStyle(int mBannerStyle) {
        this.mBannerStyle = mBannerStyle;
        return this;
    }

    public BannerView setViewPagerIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    public BannerView setImages(List<?> imageUrls) {
        this.imageUrls = imageUrls;
        this.count = imageUrls.size();
        return this;
    }

    public void update(List<?> imageUrls, List<String> mTitles) {
        this.imageUrls.clear();
        this.mTitles.clear();
        this.imageUrls.addAll(imageUrls);
        this.mTitles.addAll(mTitles);
        this.count = this.imageUrls.size();
        start();
    }

    public void update(List<?> imageUrls) {
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        start();
    }

    public void updateStyle(int mBannerStyle) {
        indicator.setVisibility(GONE);
        numIndicator.setVisibility(GONE);
        numIndicatorInside.setVisibility(GONE);
        indicatorInside.setVisibility(GONE);
        bannerTitle.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
        this.mBannerStyle = mBannerStyle;
        start();
    }

    public BannerView start() {
        setBannerStyleUI();
        setImageList(imageUrls);
        setData();
        return this;
    }

    private void setTitleStyleUI() {
        if (mTitles.size() != imageUrls.size()) {
            throw new RuntimeException("[Banner] --> The number of mTitles and images is different");
        }
        if (mTitleBackground != -1) {
            titleView.setBackgroundColor(mTitleBackground);
        }
        if (mTitleHeight != -1) {
            titleView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTitleHeight));
        }
        if (mTitleTextColor != -1) {
            bannerTitle.setTextColor(mTitleTextColor);
        }
        if (mTitleTextSize != -1) {
            bannerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        }
        if (mTitles != null && mTitles.size() > 0) {
            bannerTitle.setText(mTitles.get(0));
            bannerTitle.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
        }
    }

    private void setBannerStyleUI() {
        int visibility;
        if (count > 1) visibility = View.VISIBLE;
        else visibility = View.GONE;
        switch (mBannerStyle) {
            case BannerStyle.CIRCLE_INDICATOR:
                indicator.setVisibility(visibility);
                break;
            case BannerStyle.NUM_INDICATOR:
                numIndicator.setVisibility(visibility);
                break;
            case BannerStyle.NUM_INDICATOR_TITLE:
                numIndicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerStyle.CIRCLE_INDICATOR_TITLE:
                indicator.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE:
                indicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
        }
    }

    private void initImages() {
        imageViews.clear();
        if (mBannerStyle == BannerStyle.CIRCLE_INDICATOR ||
                mBannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE ||
                mBannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE) {
            createIndicator();
        } else if (mBannerStyle == BannerStyle.NUM_INDICATOR_TITLE) {
            numIndicatorInside.setText("1/" + count);
        } else if (mBannerStyle == BannerStyle.NUM_INDICATOR) {
            numIndicator.setText("1/" + count);
        }
    }

    private void setImageList(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            Log.e(TAG, "Please set the images data.");
            return;
        }
        initImages();
        for (int i = 0; i <= count + 1; i++) {
            View imageView = null;
            if (mImageLoader != null) {
                imageView = mImageLoader.createImageView(getContext());
            }
            if (imageView == null) {
                imageView = new ImageView(getContext());
            }
            setScaleType(imageView);
            Object url = null;
            if (i == 0) {
                url = imagesUrl.get(count - 1);
            } else if (i == count + 1) {
                url = imagesUrl.get(0);
            } else {
                url = imagesUrl.get(i - 1);
            }
            imageViews.add(imageView);
            if (mImageLoader != null)
                mImageLoader.displayImage(getContext(), url, imageView);
            else
                Log.e(TAG, "Please set images loader.");
        }
    }

    private void setScaleType(View imageView) {
        if (imageView instanceof ImageView) {
            ImageView view = ((ImageView) imageView);
            switch (mScaleType) {
                case 0:
                    view.setScaleType(ScaleType.CENTER);
                    break;
                case 1:
                    view.setScaleType(ScaleType.CENTER_CROP);
                    break;
                case 2:
                    view.setScaleType(ScaleType.CENTER_INSIDE);
                    break;
                case 3:
                    view.setScaleType(ScaleType.FIT_CENTER);
                    break;
                case 4:
                    view.setScaleType(ScaleType.FIT_END);
                    break;
                case 5:
                    view.setScaleType(ScaleType.FIT_START);
                    break;
                case 6:
                    view.setScaleType(ScaleType.FIT_XY);
                    break;
                case 7:
                    view.setScaleType(ScaleType.MATRIX);
                    break;
            }
        }
    }

    private void createIndicator() {
        indicatorImages.clear();
        indicator.removeAllViews();
        indicatorInside.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            indicatorImages.add(imageView);
            if (mBannerStyle == BannerStyle.CIRCLE_INDICATOR ||
                    mBannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE)
                indicator.addView(imageView, params);
            else if (mBannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE)
                indicatorInside.addView(imageView, params);
        }
    }


    private void setData() {
        currentItem = 1;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            mViewPager.addOnPageChangeListener(this);
        }
        mViewPager.setAdapter(adapter);
        mViewPager.setFocusable(true);
        mViewPager.setCurrentItem(1);
        indicator.setGravity(mGravity);
        if (isScroll && count > 1) {
            mViewPager.setScrollable(true);
        } else {
            mViewPager.setScrollable(false);
        }
        if (isAutoPlay)
            startAutoPlay();
    }


    public void startAutoPlay() {
        mHandler.removeCallbacks(task);
        mHandler.postDelayed(task, mDelayTime);
    }

    public void stopAutoPlay() {
        mHandler.removeCallbacks(task);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    mViewPager.setCurrentItem(currentItem, false);
                    mHandler.post(task);
                } else {
                    mViewPager.setCurrentItem(currentItem);
                    mHandler.postDelayed(task, mDelayTime);
                }
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    public int toRealPosition(int position) {
        int realPosition = (position - 1) % count;
        if (realPosition < 0)
            realPosition += count;
        return realPosition;
    }

    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(imageViews.get(position));
            View view = imageViews.get(position);
            if (listener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClicked(toRealPosition(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        currentItem = mViewPager.getCurrentItem();
        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    mViewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    mViewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mBannerStyle == BannerStyle.CIRCLE_INDICATOR ||
                mBannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE ||
                mBannerStyle == BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE) {
            indicatorImages.get((lastPosition - 1 + count) % count).setImageResource(mIndicatorUnselectedResId);
            indicatorImages.get((position - 1 + count) % count).setImageResource(mIndicatorSelectedResId);
            lastPosition = position;
        }
        if (position == 0) position = count;
        if (position > count) position = 1;
        switch (mBannerStyle) {
            case BannerStyle.CIRCLE_INDICATOR:
                break;
            case BannerStyle.NUM_INDICATOR:
                numIndicator.setText(position + "/" + count);
                break;
            case BannerStyle.NUM_INDICATOR_TITLE:
                numIndicatorInside.setText(position + "/" + count);
                bannerTitle.setText(mTitles.get(position - 1));
                break;
            case BannerStyle.CIRCLE_INDICATOR_TITLE:
                bannerTitle.setText(mTitles.get(position - 1));
                break;
            case BannerStyle.CIRCLE_INDICATOR_TITLE_INSIDE:
                bannerTitle.setText(mTitles.get(position - 1));
                break;
        }

    }

    public BannerView setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void releaseBanner() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
}
