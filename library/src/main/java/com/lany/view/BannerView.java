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
    private static final String TAG = BannerView.class.getSimpleName();
    private SparseArray<ImageView> mItemArrays = new SparseArray<>();
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;

    private LoopViewPager mViewPager;
    private TextView mTitleText;

    /**
     * 提示文字的大小
     */
    private int mTitleTextSize;

    /**
     * 提示文字的颜色，默认是白色
     */
    private int mTitleTextColor = Color.WHITE;

    /**
     * 存放点的容器
     */
    private LinearLayout mIndicatorContainerLl;
    /**
     * 点的drawable资源id
     */
    private int mIndicatorDrawableResId = R.drawable.banner_indicator_oval;

    /**
     * 点在容器中的layout的属性
     */
    private int mIndicatorGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mIndicatorLeftRightMargin;
    private int mIndicatorTopBottomMargin;
    private int mIndicatorContainerLeftRightPadding;

    /**
     * 存放TipTextView和mIndicatorContainerLl的相对布局的背景资源Id；
     */
    private Drawable mIndicatorContainerBackgroundDrawable;

    /**
     * 存放轮播信息的数据集合
     */
    protected List mData = new ArrayList<>();

    /**
     * 自动播放的间隔
     */
    private int mAutoPlayInterval = 3;

    /**
     * 页面切换的时间（从下一页开始出现，到完全出现的时间）
     */
    private int mPageChangeDuration = 300;
    /**
     * 是否正在播放
     */
    private boolean mPlaying = false;

    /**
     * 当前的页面的位置
     */
    protected int currentPosition;

    /**
     * Banner控件的适配器
     */
    private BannerAdapter mBannerAdapter;

    /**
     * 任务执行器
     */
    protected ScheduledExecutorService mExecutor;


    /**
     * 播放下一个执行器
     */
    private Handler mPlayHandler = new PlayHandler(this);


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //默认点指示器的左右Margin3dp
        mIndicatorLeftRightMargin = dp2px(3);
        //默认点指示器的上下margin为6dp
        mIndicatorTopBottomMargin = dp2px(6);
        //默认点容器的左右padding为10dp
        mIndicatorContainerLeftRightPadding = dp2px(10);
        //默认指示器提示文字大小8sp
        mTitleTextSize = sp2px(8);
        //默认指示器容器的背景图片
        mIndicatorContainerBackgroundDrawable = new ColorDrawable(Color.parseColor("#33aaaaaa"));

        //初始化自定义属性
        initCustomAttrs(context, attrs);

        //控件初始化
        initView();
    }

    /**
     * 初始化自定义属性
     *
     * @param context context
     * @param attrs   attrs
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BannerView_bv_indicator) {
            //指示器点的样式资源id
            mIndicatorDrawableResId = typedArray.getResourceId(attr, R.drawable.banner_indicator_oval);
        } else if (attr == R.styleable.BannerView_bv_indicator_container_bg) {
            //指示器容器背景样式
            mIndicatorContainerBackgroundDrawable = typedArray.getDrawable(attr);

        } else if (attr == R.styleable.BannerView_bv_indicator_left_right_margin) {
            //指示器左右边距
            mIndicatorLeftRightMargin = typedArray.getDimensionPixelSize(attr, mIndicatorLeftRightMargin);
        } else if (attr == R.styleable.BannerView_bv_indicator_padding) {
            //指示器容器的左右padding
            mIndicatorContainerLeftRightPadding = typedArray.getDimensionPixelSize(attr, mIndicatorContainerLeftRightPadding);
        } else if (attr == R.styleable.BannerView_bv_indicator_top_bottom_margin) {

            //指示器的上下margin
            mIndicatorTopBottomMargin = typedArray.getDimensionPixelSize(attr, mIndicatorTopBottomMargin);
        } else if (attr == R.styleable.BannerView_bv_indicator_gravity) {
            //指示器在容器中的位置属性
            mIndicatorGravity = typedArray.getInt(attr, mIndicatorGravity);
        } else if (attr == R.styleable.BannerView_bv_play_interval) {
            //轮播的间隔
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        } else if (attr == R.styleable.BannerView_bv_page_change_duration) {
            //页面切换的持续时间
            mPageChangeDuration = typedArray.getInteger(attr, mPageChangeDuration);
        } else if (attr == R.styleable.BannerView_bv_title_text_color) {
            //提示文字颜色
            mTitleTextColor = typedArray.getColor(attr, mTitleTextColor);
        } else if (attr == R.styleable.BannerView_bv_title_text_size) {
            mTitleTextSize = typedArray.getDimensionPixelSize(attr, mTitleTextSize);
        }

    }

    private void initView() {
        //初始化ViewPager
        mViewPager = new LoopViewPager(getContext());

        mViewPager.setOffscreenPageLimit(4);

        //以matchParent的方式将viewPager填充到控件容器中
        addView(mViewPager, new LayoutParams(RMP, RMP));

        //修正banner页面切换时间
        mPageChangeDuration = mPageChangeDuration > (mAutoPlayInterval * 1000) ? (mAutoPlayInterval * 1000) : mPageChangeDuration;

        // 设置banner轮播的切换时间
        BannerScroller pagerScroller = new BannerScroller(getContext());
        pagerScroller.changScrollDuration(mViewPager, mPageChangeDuration);


        //创建指示器容器的相对布局
        RelativeLayout indicatorContainerRl = new RelativeLayout(getContext());
        //设置指示器容器的背景
        if (Build.VERSION.SDK_INT >= 16) {
            indicatorContainerRl.setBackground(mIndicatorContainerBackgroundDrawable);
        } else {
            indicatorContainerRl.setBackgroundDrawable(mIndicatorContainerBackgroundDrawable);
        }
        //设置指示器容器Padding
        indicatorContainerRl.setPadding(mIndicatorContainerLeftRightPadding, 0, mIndicatorContainerLeftRightPadding, 0);
        //初始化指示器容器的布局参数
        LayoutParams indicatorContainerLp = new LayoutParams(RMP, RWC);
        // 设置指示器容器内的子view的布局方式
        if ((mIndicatorGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            indicatorContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            indicatorContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        //将指示器容器添加到父View中
        addView(indicatorContainerRl, indicatorContainerLp);


        //初始化存放点的容器线性布局
        mIndicatorContainerLl = new LinearLayout(getContext());
        //设置点容器布局的id
        mIndicatorContainerLl.setId(R.id.banner_indicator_container_id);
        //设置线性布局的方
        mIndicatorContainerLl.setOrientation(LinearLayout.HORIZONTAL);
        //设置点容器的布局参数
        LayoutParams pointContainerLp = new LayoutParams(RWC, RWC);
        //将点容器存放到指示器容器中
        indicatorContainerRl.addView(mIndicatorContainerLl, pointContainerLp);


        //初始化tip的layout尺寸参数，高度和点的高度一致
        LayoutParams tipLp = new LayoutParams(RMP, getResources().getDrawable(mIndicatorDrawableResId).getIntrinsicHeight() + 2 * mIndicatorTopBottomMargin);
        mTitleText = new TextView(getContext());
        mTitleText.setGravity(Gravity.CENTER_VERTICAL);
        mTitleText.setSingleLine(true);
        mTitleText.setEllipsize(TextUtils.TruncateAt.END);
        mTitleText.setTextColor(mTitleTextColor);
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        //将TieTextView存放于指示器容器中
        indicatorContainerRl.addView(mTitleText, tipLp);
        int horizontalGravity = mIndicatorGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        // 处理圆点容器位于指示器容器的左边、右边还是水平居中
        if (horizontalGravity == Gravity.LEFT) {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            //提示文字设置在点容器的右边
            tipLp.addRule(RelativeLayout.RIGHT_OF, R.id.banner_indicator_container_id);
            mTitleText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else if (horizontalGravity == Gravity.RIGHT) {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_indicator_container_id);
        } else {
            pointContainerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_indicator_container_id);
        }
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    /**
     * 初始化点
     * 这样的做法，可以使在刷新获数据的时候提升性能
     */
    private void initPoints() {
        //获取容器中原有点的数量
        int childCount = mIndicatorContainerLl.getChildCount();
        //获取目标点的数据量
        int dataSize = mData.size();
        //获取增加获取删减点的数量
        int offset = dataSize - childCount;
        if (offset == 0)
            return;
        if (offset > 0) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
            lp.setMargins(mIndicatorLeftRightMargin, mIndicatorTopBottomMargin, mIndicatorLeftRightMargin, mIndicatorTopBottomMargin);
            ImageView imageView;
            for (int i = 0; i < offset; i++) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(lp);
                imageView.setImageResource(mIndicatorDrawableResId);
                imageView.setEnabled(false);
                mIndicatorContainerLl.addView(imageView);
            }
            return;
        }
        if (offset < 0) {
            mIndicatorContainerLl.removeViews(dataSize, -offset);
        }
    }

    private final class ChangePointListener extends LoopViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            currentPosition = position % mData.size();
            switchToPoint(currentPosition);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mTitleText != null) {
                if (positionOffset > 0.5) {
                    mBannerAdapter.selectTips(mTitleText, currentPosition);
                    mTitleText.setAlpha(positionOffset);
                } else {
                    mTitleText.setAlpha(1 - positionOffset);
                    mBannerAdapter.selectTips(mTitleText, currentPosition);
                }
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
        for (int i = 0; i < mIndicatorContainerLl.getChildCount(); i++) {
            mIndicatorContainerLl.getChildAt(i).setEnabled(false);
        }
        mIndicatorContainerLl.getChildAt(newCurrentPoint).setEnabled(true);

        if (mTitleText != null) {
            mBannerAdapter.selectTips(mTitleText, currentPosition);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
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
            return mData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = createItemView(position);
            mBannerAdapter.setImageViewSource(view, position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
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
            if (position != 0 && position != mData.size() - 1) {
                mItemArrays.put(position, iv);
            }
        }
        return iv;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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
     * 判断控件是否可用
     *
     * @return
     */
    private boolean isValid() {
        if (mViewPager == null) {
            Log.e(TAG, "ViewPager is not exist!");
            return false;
        }
        if (mData == null || mData.size() == 0) {
            Log.e(TAG, "DataList must be not empty!");
            return false;
        }
        return true;
    }

    /**
     * 设置数据的集合
     */
    private void setSource() {
        List list = mBannerAdapter.getItems();
        if (list == null) {
            Log.d(TAG, "setSource: list==null");
            return;
        }
        this.mData = list;
        setAdapter();
    }

    /**
     * 给viewpager设置适配器
     */
    private void setAdapter() {
        mViewPager.setAdapter(new InnerPagerAdapter());
        mViewPager.addOnPageChangeListener(new ChangePointListener());
    }

    public void setAdapter(BannerAdapter adapter) {
        mBannerAdapter = adapter;
        setSource();
        notifyDataHasChanged();
    }


    /**
     * 通知数据已经放生改变
     */
    public void notifyDataHasChanged() {
        initPoints();
        mViewPager.getAdapter().notifyDataSetChanged();
        mViewPager.setCurrentItem(0, false);
        if (mData.size() > 1)
            goScroll();
    }


    /**
     * 静态内部类，防止发生内存泄露
     */
    private static class PlayHandler extends Handler {
        WeakReference<BannerView> mWeakBanner;

        public PlayHandler(BannerView banner) {
            this.mWeakBanner = new WeakReference<>(banner);
        }

        @Override
        public void handleMessage(Message msg) {
            BannerView weakBanner = mWeakBanner.get();
            if (weakBanner != null)
                weakBanner.scrollToNextItem(weakBanner.currentPosition);
        }
    }
}
