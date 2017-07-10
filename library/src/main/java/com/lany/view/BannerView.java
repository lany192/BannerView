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
import android.support.v4.view.LoopViewPager;
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
    private LinearLayout mIndicatorContainer;
    private int mIndicatorResId = R.drawable.banner_indicator_oval;
    private int mIndicatorGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mIndicatorLeftRightMargin;
    private int mIndicatorTopBottomMargin;
    private int mIndicatorContainerLeftRightPadding;
    private Drawable mIndicatorContainerBgDrawable;
    private List mItems = new ArrayList<>();
    private int mAutoPlayInterval = 3;
    private boolean mPlaying = false;
    private int currentPosition;
    private BannerAdapter mBannerAdapter;
    private ScheduledExecutorService mExecutor;

    private Handler mPlayHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scrollToNextItem(currentPosition);
        }
    };
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
        mIndicatorLeftRightMargin = dp2px(3);
        mIndicatorTopBottomMargin = dp2px(6);
        mIndicatorContainerLeftRightPadding = dp2px(10);
        mTitleTextSize = sp2px(8);
        mIndicatorContainerBgDrawable = new ColorDrawable(Color.parseColor("#33aaaaaa"));

        initCustomAttrs(attrs);
        mViewPager = new LoopViewPager(getContext());
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        RelativeLayout indicatorContainerRl = new RelativeLayout(getContext());
        if (Build.VERSION.SDK_INT >= 16) {
            indicatorContainerRl.setBackground(mIndicatorContainerBgDrawable);
        } else {
            indicatorContainerRl.setBackgroundDrawable(mIndicatorContainerBgDrawable);
        }
        indicatorContainerRl.setPadding(mIndicatorContainerLeftRightPadding, 0, mIndicatorContainerLeftRightPadding, 0);
        LayoutParams indicatorContainerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        if ((mIndicatorGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            indicatorContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            indicatorContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        addView(indicatorContainerRl, indicatorContainerLp);

        mIndicatorContainer = new LinearLayout(getContext());
        mIndicatorContainer.setId(R.id.banner_indicator_container_id);
        mIndicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams indicatorContainerLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicatorContainerRl.addView(mIndicatorContainer, indicatorContainerLP);

        if (isShowIndicator) {
            mIndicatorContainer.setVisibility(VISIBLE);
        } else {
            mIndicatorContainer.setVisibility(GONE);
        }

        int height = getResources().getDrawable(mIndicatorResId).getIntrinsicHeight() + 2 * mIndicatorTopBottomMargin;
        LayoutParams titleLP = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        mTitleText = new TextView(getContext());
        mTitleText.setGravity(Gravity.CENTER_VERTICAL);
        mTitleText.setSingleLine(true);
        mTitleText.setEllipsize(TextUtils.TruncateAt.END);
        mTitleText.setTextColor(mTitleTextColor);
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        indicatorContainerRl.addView(mTitleText, titleLP);
        int horizontalGravity = mIndicatorGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        if (horizontalGravity == Gravity.LEFT) {
            indicatorContainerLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
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

    private final class ChangePointListener extends ViewPager.SimpleOnPageChangeListener {

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

    private void switchToPoint(int newCurrentPoint) {
        for (int i = 0; i < mIndicatorContainer.getChildCount(); i++) {
            if (newCurrentPoint == i) {
                mIndicatorContainer.getChildAt(i).setEnabled(true);
            } else {
                mIndicatorContainer.getChildAt(i).setEnabled(false);
            }
        }
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

    private void scrollToNextItem(int position) {
        position++;
        if (position >= mItems.size()) {
            position = currentPosition = 0;
        }
        mViewPager.setCurrentItem(position, true);
    }

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

    public void goScroll() {
        if (mViewPager == null) {
            Log.e(TAG, "ViewPager is not exist!");
            return;
        }
        if (isEmpty(mItems)) {
            Log.e(TAG, "items must be not empty!");
            return;
        }
        if (mPlaying) {
            return;
        } else {
            pauseScroll();
            mExecutor = Executors.newSingleThreadScheduledExecutor();
            mExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mPlayHandler.obtainMessage().sendToTarget();
                }
            }, mAutoPlayInterval, mAutoPlayInterval, TimeUnit.SECONDS);
            mPlaying = true;
        }
    }

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

    public void setAdapter(@NonNull BannerAdapter adapter) {
        mBannerAdapter = adapter;
        List list = mBannerAdapter.getItems();
        if (isEmpty(list)) {
            Log.e(TAG, "items must be not empty!");
            return;
        }
        this.mItems.clear();
        this.mItems.addAll(list);
        currentPosition = 0;
        mIndicatorContainer.removeAllViews();
        mItemArrays.clear();
        InnerPagerAdapter innerPagerAdapter = new InnerPagerAdapter();
        mViewPager.setAdapter(innerPagerAdapter);
        mViewPager.addOnPageChangeListener(new ChangePointListener());
        if (!isEmpty(mItems) && mItems.size() > 1) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(mIndicatorLeftRightMargin, mIndicatorTopBottomMargin, mIndicatorLeftRightMargin, mIndicatorTopBottomMargin);
            ImageView indicator;
            for (int i = 0; i < mItems.size(); i++) {
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
        }
        innerPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(0, false);
        if (!isEmpty(mItems)) {
            goScroll();
        }
    }

    boolean isEmpty(List<?> lists) {
        return lists == null || lists.size() == 0;
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (null != listener) {
            mViewPager.addOnPageChangeListener(listener);
        }
    }
}
