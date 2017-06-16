package com.lany.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BannerView extends RelativeLayout {
    private static final String TAG = "BannerView";
    private SparseArray<ImageView> mItemArrays = new SparseArray<>();
    private LoopViewPager mViewPager;
    private TextView mTitleText;
    private int mTitleTextSize;
    private int mTitleTextColor = Color.WHITE;
    /**
     * viewpager indicator container
     */
    private LinearLayout mIndicatorContainer;
    /**
     * viewpager indicator drawable resource id
     */
    private int mIndicatorResId = R.drawable.banner_indicator_oval;
    /**
     * viewpager indicator container gravity attr
     */
    private int mIndicatorGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    /**
     * viewpager indicator margin for left and right
     */
    private int mIndicatorLeftRightMargin;
    /**
     * viewpager indicator margin for top and bottom
     */
    private int mIndicatorTopBottomMargin;
    /**
     * viewpager indicator container padding for left and right
     */
    private int mIndicatorContainerLeftRightPadding;
    /**
     * indicator and title container background
     */
    private Drawable mIndicatorContainerBgDrawable;
    /**
     * banner items
     */
    protected List mItems = new ArrayList<>();
    /**
     * Automatically broadcast interval
     */
    private int mAutoPlayInterval = 3;
    /**
     * 页面切换的时间（从下一页开始出现，到完全出现的时间）
     */
    private int mPageChangeDuration = 300;
    /**
     * play flag
     */
    private boolean mPlaying = false;
    /**
     * viewpager current position
     */
    protected int currentPosition;

    private BannerAdapter mBannerAdapter;

    /**
     * 任务执行器
     */
    protected ScheduledExecutorService mExecutor;


    /**
     * 播放下一个执行器
     */
    private Handler mPlayHandler = new PlayHandler(this);

    private boolean isShowIndicator = true;


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //默认点指示器的左右Margin3dp
        mIndicatorLeftRightMargin = dp2px(3);
        //默认点指示器的上下margin为6dp
        mIndicatorTopBottomMargin = dp2px(6);
        //默认点容器的左右padding为10dp
        mIndicatorContainerLeftRightPadding = dp2px(10);
        //默认指示器提示文字大小8sp
        mTitleTextSize = sp2px(8);
        //默认指示器容器的背景图片
        mIndicatorContainerBgDrawable = new ColorDrawable(Color.parseColor("#33aaaaaa"));

        //初始化自定义属性
        initCustomAttrs(attrs);

        //初始化ViewPager
        mViewPager = new LoopViewPager(getContext());
        //以matchParent的方式将viewPager填充到控件容器中
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //修正banner页面切换时间
        mPageChangeDuration = mPageChangeDuration > (mAutoPlayInterval * 1000) ? (mAutoPlayInterval * 1000) : mPageChangeDuration;

        // 设置banner轮播的切换时间
        BannerScroller pagerScroller = new BannerScroller(getContext());
        pagerScroller.changScrollDuration(mViewPager, mPageChangeDuration);


        //创建指示器容器的相对布局
        RelativeLayout indicatorContainerRl = new RelativeLayout(getContext());
        //设置指示器容器的背景
        if (Build.VERSION.SDK_INT >= 16) {
            indicatorContainerRl.setBackground(mIndicatorContainerBgDrawable);
        } else {
            indicatorContainerRl.setBackgroundDrawable(mIndicatorContainerBgDrawable);
        }
        //设置指示器容器Padding
        indicatorContainerRl.setPadding(mIndicatorContainerLeftRightPadding, 0, mIndicatorContainerLeftRightPadding, 0);
        //初始化指示器容器的布局参数
        LayoutParams indicatorContainerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 设置指示器容器内的子view的布局方式
        if ((mIndicatorGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            indicatorContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            indicatorContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        //将指示器容器添加到父View中
        addView(indicatorContainerRl, indicatorContainerLp);


        //初始化存放点的容器线性布局
        mIndicatorContainer = new LinearLayout(getContext());
        //设置点容器布局的id
        mIndicatorContainer.setId(R.id.banner_indicator_container_id);
        //设置线性布局的方
        mIndicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
        //设置点容器的布局参数
        LayoutParams indicatorContainerLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //将点容器存放到指示器容器中
        indicatorContainerRl.addView(mIndicatorContainer, indicatorContainerLP);

        if (isShowIndicator) {
            mIndicatorContainer.setVisibility(VISIBLE);
        } else {
            mIndicatorContainer.setVisibility(GONE);
        }

        //初始化tip的layout尺寸参数，高度和点的高度一致
        int height = getResources().getDrawable(mIndicatorResId).getIntrinsicHeight() + 2 * mIndicatorTopBottomMargin;
        LayoutParams titleLP = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        mTitleText = new TextView(getContext());
        mTitleText.setGravity(Gravity.CENTER_VERTICAL);
        mTitleText.setSingleLine(true);
        mTitleText.setEllipsize(TextUtils.TruncateAt.END);
        mTitleText.setTextColor(mTitleTextColor);
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        //将TieTextView存放于指示器容器中
        indicatorContainerRl.addView(mTitleText, titleLP);
        int horizontalGravity = mIndicatorGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        // 处理圆点容器位于指示器容器的左边、右边还是水平居中
        if (horizontalGravity == Gravity.LEFT) {
            indicatorContainerLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            //提示文字设置在点容器的右边
            titleLP.addRule(RelativeLayout.RIGHT_OF, R.id.banner_indicator_container_id);
            mTitleText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else if (horizontalGravity == Gravity.RIGHT) {
            indicatorContainerLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            titleLP.addRule(RelativeLayout.LEFT_OF, R.id.banner_indicator_container_id);
        } else {
            indicatorContainerLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
            titleLP.addRule(RelativeLayout.LEFT_OF, R.id.banner_indicator_container_id);
        }
    }

    private void initCustomAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BannerView_bv_indicator) {
            mIndicatorResId = typedArray.getResourceId(attr, R.drawable.banner_indicator_oval);
        } else if (attr == R.styleable.BannerView_bv_indicator_container_bg) {
            mIndicatorContainerBgDrawable = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.BannerView_bv_indicator_left_right_margin) {
            mIndicatorLeftRightMargin = typedArray.getDimensionPixelSize(attr, mIndicatorLeftRightMargin);
        } else if (attr == R.styleable.BannerView_bv_indicator_padding) {
            mIndicatorContainerLeftRightPadding = typedArray.getDimensionPixelSize(attr, mIndicatorContainerLeftRightPadding);
        } else if (attr == R.styleable.BannerView_bv_indicator_top_bottom_margin) {
            mIndicatorTopBottomMargin = typedArray.getDimensionPixelSize(attr, mIndicatorTopBottomMargin);
        } else if (attr == R.styleable.BannerView_bv_indicator_gravity) {
            mIndicatorGravity = typedArray.getInt(attr, mIndicatorGravity);
        } else if (attr == R.styleable.BannerView_bv_play_interval) {
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        } else if (attr == R.styleable.BannerView_bv_page_change_duration) {
            mPageChangeDuration = typedArray.getInteger(attr, mPageChangeDuration);
        } else if (attr == R.styleable.BannerView_bv_title_text_color) {
            mTitleTextColor = typedArray.getColor(attr, mTitleTextColor);
        } else if (attr == R.styleable.BannerView_bv_title_text_size) {
            mTitleTextSize = typedArray.getDimensionPixelSize(attr, mTitleTextSize);
        } else if (attr == R.styleable.BannerView_bv_is_show_indicator) {
            isShowIndicator = typedArray.getBoolean(attr, true);
        }
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    private final class ChangePointListener extends LoopViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            currentPosition = position % mItems.size();
            switchToPoint(currentPosition);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mTitleText != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (positionOffset > 0.5) {
                        mTitleText.setAlpha(positionOffset);
                    } else {
                        mTitleText.setAlpha(1 - positionOffset);
                    }
                }
                mBannerAdapter.bind(createItemView(currentPosition), mTitleText, currentPosition);
            }
        }
    }

    /**
     * 将点切换到指定的位置
     * 就是将指定位置的点设置成Enable
     *
     * @param newCurrentPoint 新位置
     */
    private void switchToPoint(int newCurrentPoint) {
        for (int i = 0; i < mIndicatorContainer.getChildCount(); i++) {
            mIndicatorContainer.getChildAt(i).setEnabled(false);
        }
        mIndicatorContainer.getChildAt(newCurrentPoint).setEnabled(true);
        if (mTitleText != null) {
            mBannerAdapter.bind(createItemView(currentPosition), mTitleText, currentPosition);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseScroll();
                break;
            case MotionEvent.ACTION_MOVE:
                pauseScroll();
                break;
            case MotionEvent.ACTION_UP:
                goScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
                goScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 设置页码切换过程的时间长度
     *
     * @param duration 页码切换过程的时间长度
     */
    public void setPageChangeDuration(int duration) {
        mPageChangeDuration = duration;
    }

    /**
     * 滚动到下一个条目
     *
     * @param position
     */
    private void scrollToNextItem(int position) {
        position++;
        mViewPager.setCurrentItem(position, true);
    }


    /**
     * viewPager的适配器
     */
    private final class InnerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = createItemView(position);
            mBannerAdapter.bind(view, mTitleText, position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBannerAdapter.selectClicked(position);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * 创建itemView
     *
     * @param position
     * @return
     */
    private ImageView createItemView(int position) {
        ImageView iv = mItemArrays.get(position);
        if (iv == null) {
            iv = new ImageView(getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (position != 0 && position != mItems.size() - 1) {
                mItemArrays.put(position, iv);
            }
        }
        return iv;
    }


    /**
     * 方法使用状态 ：viewpager处于暂停的状态
     * 开始滚动
     */
    public void goScroll() {
        if (!isValid()) {
            return;
        }
        if (mPlaying) {
            return;
        } else {
            pauseScroll();
            mExecutor = Executors.newSingleThreadScheduledExecutor();
            //command：执行线程
            //initialDelay：初始化延时
            //period：两次开始执行最小间隔时间
            //unit：计时单位
            mExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mPlayHandler.obtainMessage().sendToTarget();
                }
            }, mAutoPlayInterval, mAutoPlayInterval, TimeUnit.SECONDS);
            mPlaying = true;
        }
    }


    /**
     * 暂停滚动
     */
    public void pauseScroll() {
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }
        mPlaying = false;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            goScroll();
        } else if (visibility == INVISIBLE) {
            pauseScroll();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pauseScroll();
    }

    /**
     * 检查控件是否可用
     *
     * @return 控件是否可用
     */
    private boolean isValid() {
        if (mViewPager == null) {
            Log.e(TAG, "ViewPager is not exist!");
            return false;
        }
        if (mItems == null || mItems.size() == 0) {
            Log.e(TAG, "DataList must be not empty!");
            return false;
        }
        return true;
    }

    public void setAdapter(@NonNull BannerAdapter adapter) {
        mBannerAdapter = adapter;
        List list = mBannerAdapter.getItems();
        if (list == null) {
            Log.d(TAG, "setSource: list==null");
            return;
        }
        this.mItems = list;
        mViewPager.setAdapter(new InnerPagerAdapter());
        mViewPager.addOnPageChangeListener(new ChangePointListener());
        //获取容器中原有点的数量
        int childCount = mIndicatorContainer.getChildCount();
        //获取目标点的数据量
        int dataSize = mItems.size();
        if (dataSize > 1) {
            //获取增加获取删减点的数量
            int offset = dataSize - childCount;
            if (offset == 0)
                return;
            if (offset > 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(mIndicatorLeftRightMargin, mIndicatorTopBottomMargin, mIndicatorLeftRightMargin, mIndicatorTopBottomMargin);
                ImageView indicator;
                for (int i = 0; i < offset; i++) {
                    indicator = new ImageView(getContext());
                    indicator.setLayoutParams(lp);
                    indicator.setImageResource(mIndicatorResId);
                    if (i == 0) {
                        indicator.setEnabled(true);
                    } else {
                        indicator.setEnabled(false);
                    }
                    mIndicatorContainer.addView(indicator);
                }
                return;
            }
            if (offset < 1) {
                mIndicatorContainer.removeViews(dataSize, -offset);
            }
        } else {
            mIndicatorContainer.removeAllViews();
        }
        mViewPager.getAdapter().notifyDataSetChanged();
        mViewPager.setCurrentItem(0, false);
        if (mItems.size() > 1) {
            goScroll();
        }
    }

    private static class PlayHandler extends Handler {
        WeakReference<BannerView> mWeakBanner;

        public PlayHandler(BannerView banner) {
            this.mWeakBanner = new WeakReference<>(banner);
        }

        @Override
        public void handleMessage(Message msg) {
            BannerView weakBanner = mWeakBanner.get();
            if (weakBanner != null) {
                weakBanner.scrollToNextItem(weakBanner.currentPosition);
            }
        }
    }

    /**
     * 外部添加banner切换监听
     *
     * @param listener 监听器
     */
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (null != listener) {
            mViewPager.addOnPageChangeListener(listener);
        }
    }
}
