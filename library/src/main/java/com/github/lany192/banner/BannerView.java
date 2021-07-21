package com.github.lany192.banner;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 可以无限换行banner
 * https://github.com/lany192/BannerView
 *
 * @author lany192
 */
@Keep
public class BannerView extends RelativeLayout {
    private static final long DEFAULT_AUTO_TIME = 2500;
    private static final long DEFAULT_PAGER_DURATION = 800;
    private static final int NORMAL_COUNT = 2;

    private ViewPager2.OnPageChangeCallback changeCallback;
    private CompositePageTransformer compositePageTransformer;
    private BannerAdapterWrapper adapterWrapper;
    private ViewPager2 viewPager2;
    private Indicator indicator;
    private boolean isAutoPlay = true;
    private boolean isBeginPagerChange = true;
    private boolean isTaskPostDelayed;
    private long autoTurningTime = DEFAULT_AUTO_TIME;
    private long pagerScrollDuration = DEFAULT_PAGER_DURATION;
    private int needPage = NORMAL_COUNT;
    private int sidePage = needPage / NORMAL_COUNT;
    private int tempPosition;

    private float startX, startY, lastX, lastY;
    private final int scaledTouchSlop;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() >> 1;
        initViews(context);
    }

    private void initViews(final Context context) {
        viewPager2 = new ViewPager2(context);
        viewPager2.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        viewPager2.setPageTransformer(compositePageTransformer = new CompositePageTransformer());
        viewPager2.registerOnPageChangeCallback(new OnPageChangeCallback());
        viewPager2.setAdapter(adapterWrapper = new BannerAdapterWrapper());
        setOffscreenPageLimit(1);
        initViewPagerScrollProxy();
        addView(viewPager2);
    }

    private void startPager(int startPosition) {
        if (sidePage == NORMAL_COUNT) {
            viewPager2.setAdapter(adapterWrapper);
        } else {
            adapterWrapper.notifyDataSetChanged();
        }
        setCurrentItem(startPosition, false);
        if (indicator != null) {
            indicator.initIndicatorCount(getRealCount(), getCurrentPager());
        }
        if (isAutoPlay()) {
            startTurning();
        }
    }

    private int getRealCount() {
        return adapterWrapper.getRealCount();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAutoPlay()) {
            startTurning();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isAutoPlay()) {
            stopTurning();
        }
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay()) {
                tempPosition++;
                if (tempPosition == getRealCount() + sidePage + 1) {
                    isBeginPagerChange = false;
                    viewPager2.setCurrentItem(sidePage, false);
                    post(task);
                } else {
                    isBeginPagerChange = true;
                    viewPager2.setCurrentItem(tempPosition);
                    postDelayed(task, autoTurningTime);
                }
            }
        }
    };

    private int toRealPosition(int position) {
        int realPosition = 0;
        if (getRealCount() > 1) {
            realPosition = (position - sidePage) % getRealCount();
        }
        if (realPosition < 0) {
            realPosition += getRealCount();
        }
        return realPosition;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoPlay() && viewPager2.isUserInputEnabled()) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startTurning();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopTurning();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            startX = lastX = ev.getRawX();
            startY = lastY = ev.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            lastX = ev.getRawX();
            lastY = ev.getRawY();
            if (viewPager2.isUserInputEnabled()) {
                float distanceX = Math.abs(lastX - startX);
                float distanceY = Math.abs(lastY - startY);
                boolean disallowIntercept;
                if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    disallowIntercept = distanceX > scaledTouchSlop && distanceX > distanceY;
                } else {
                    disallowIntercept = distanceY > scaledTouchSlop && distanceY > distanceX;
                }
                getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
            }
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            return Math.abs(lastX - startX) > scaledTouchSlop || Math.abs(lastY - startY) > scaledTouchSlop;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private class OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = toRealPosition(position);
            if (changeCallback != null) {
                changeCallback.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
            if (indicator != null) {
                indicator.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (getRealCount() > 1) {
                tempPosition = position;
            }
            if (isBeginPagerChange) {
                int realPosition = toRealPosition(position);
                if (changeCallback != null) {
                    changeCallback.onPageSelected(realPosition);
                }
                if (indicator != null) {
                    indicator.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                if (tempPosition == sidePage - 1) {
                    isBeginPagerChange = false;
                    viewPager2.setCurrentItem(getRealCount() + tempPosition, false);
                } else if (tempPosition == getRealCount() + sidePage) {
                    isBeginPagerChange = false;
                    viewPager2.setCurrentItem(sidePage, false);
                } else {
                    isBeginPagerChange = true;
                }
            }
            if (changeCallback != null) {
                changeCallback.onPageScrollStateChanged(state);
            }
            if (indicator != null) {
                indicator.onPageScrollStateChanged(state);
            }
        }
    }

    private class BannerAdapterWrapper extends RecyclerView.Adapter<ViewHolder> {
        private RecyclerView.Adapter<ViewHolder> adapter;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            adapter.onBindViewHolder(holder, toRealPosition(position));
        }

        @Override
        public int getItemViewType(int position) {
            return adapter.getItemViewType(toRealPosition(position));
        }

        @Override
        public int getItemCount() {
            return getRealCount() > 1 ? getRealCount() + needPage : getRealCount();
        }

        @Override
        public long getItemId(int position) {
            return adapter.getItemId(toRealPosition(position));
        }

        int getRealCount() {
            return adapter == null ? 0 : adapter.getItemCount();
        }

        void registerAdapter(RecyclerView.Adapter<ViewHolder> adapter) {
            if (this.adapter != null) {
                this.adapter.unregisterAdapterDataObserver(itemDataSetChangeObserver);
            }
            this.adapter = adapter;
            if (this.adapter != null) {
                this.adapter.registerAdapterDataObserver(itemDataSetChangeObserver);
            }
        }
    }

    private final RecyclerView.AdapterDataObserver itemDataSetChangeObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public final void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            onChanged();
        }

        @Override
        public final void onItemRangeInserted(int positionStart, int itemCount) {
            if (positionStart > 1) {
                onChanged();
            }
        }

        @Override
        public final void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }

        @Override
        public void onChanged() {
            startPager(getCurrentPager());
        }
    };

    private void initViewPagerScrollProxy() {
        try {
            //控制切换速度，采用反射方。法方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            ProxyLayoutManger proxyLayoutManger = new ProxyLayoutManger(getContext(), linearLayoutManager);
            recyclerView.setLayoutManager(proxyLayoutManger);

            Field mRecyclerView = RecyclerView.LayoutManager.class.getDeclaredField("mRecyclerView");
            mRecyclerView.setAccessible(true);
            mRecyclerView.set(linearLayoutManager, recyclerView);

            Field layoutMangerField = ViewPager2.class.getDeclaredField("mLayoutManager");
            layoutMangerField.setAccessible(true);
            layoutMangerField.set(viewPager2, proxyLayoutManger);

            Field pageTransformerAdapterField = ViewPager2.class.getDeclaredField("mPageTransformerAdapter");
            pageTransformerAdapterField.setAccessible(true);
            Object mPageTransformerAdapter = pageTransformerAdapterField.get(viewPager2);
            if (mPageTransformerAdapter != null) {
                Class<?> aClass = mPageTransformerAdapter.getClass();
                Field layoutManagerField = aClass.getDeclaredField("mLayoutManager");
                layoutManagerField.setAccessible(true);
                layoutManagerField.set(mPageTransformerAdapter, proxyLayoutManger);
            }
            Field scrollEventAdapterField = ViewPager2.class.getDeclaredField("mScrollEventAdapter");
            scrollEventAdapterField.setAccessible(true);
            Object mScrollEventAdapter = scrollEventAdapterField.get(viewPager2);
            if (mScrollEventAdapter != null) {
                Class<?> aClass = mScrollEventAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mScrollEventAdapter, proxyLayoutManger);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class ProxyLayoutManger extends LinearLayoutManager {
        private final RecyclerView.LayoutManager layoutManager;

        ProxyLayoutManger(Context context, LinearLayoutManager layoutManager) {
            super(context, layoutManager.getOrientation(), false);
            this.layoutManager = layoutManager;
        }

        @Override
        public boolean performAccessibilityAction(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int action, @Nullable Bundle args) {
            return layoutManager.performAccessibilityAction(recycler, state, action, args);
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, @NonNull AccessibilityNodeInfoCompat info) {
            layoutManager.onInitializeAccessibilityNodeInfo(recycler, state, info);
        }

        @Override
        public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child, @NonNull Rect rect, boolean immediate, boolean focusedChildVisible) {
            return layoutManager.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
        }

        @Override
        protected void calculateExtraLayoutSpace(@NonNull RecyclerView.State state, @NonNull int[] extraLayoutSpace) {
            try {
                Method method = layoutManager.getClass().getDeclaredMethod("calculateExtraLayoutSpace", state.getClass(), extraLayoutSpace.getClass());
                method.setAccessible(true);
                method.invoke(layoutManager, state, extraLayoutSpace);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected int calculateTimeForDeceleration(int dx) {
                    return (int) (pagerScrollDuration * (1 - .3356));
                }
            };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

    /*--------------- 下面是对外暴露的方法 ---------------*/

    /**
     * 设置一屏多页
     *
     * @param multiWidth 左右页面露出来的宽度一致
     * @param pageMargin item与item之间的宽度
     */
    public BannerView setPageMargin(int multiWidth, int pageMargin) {
        return setPageMargin(multiWidth, multiWidth, pageMargin);
    }

    /**
     * 设置一屏多页
     *
     * @param tlWidth    左边页面显露出来的宽度
     * @param brWidth    右边页面露出来的宽度
     * @param pageMargin item与item之间的宽度
     */
    public BannerView setPageMargin(int tlWidth, int brWidth, int pageMargin) {
        if (pageMargin < 0) {
            pageMargin = 0;
        }
        addPageTransformer(new MarginPageTransformer(pageMargin));
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_VERTICAL) {
            recyclerView.setPadding(viewPager2.getPaddingLeft(), tlWidth + Math.abs(pageMargin), viewPager2.getPaddingRight(), brWidth + Math.abs(pageMargin));
        } else {
            recyclerView.setPadding(tlWidth + Math.abs(pageMargin), viewPager2.getPaddingTop(), brWidth + Math.abs(pageMargin), viewPager2.getPaddingBottom());
        }
        recyclerView.setClipToPadding(false);
        needPage = NORMAL_COUNT + NORMAL_COUNT;
        sidePage = NORMAL_COUNT;
        return this;
    }

    public BannerView addPageTransformer(ViewPager2.PageTransformer transformer) {
        compositePageTransformer.addTransformer(transformer);
        return this;
    }

    public BannerView setAutoTurningTime(long autoTurningTime) {
        this.autoTurningTime = autoTurningTime;
        return this;
    }

    public BannerView registerOnPageChangeCallback(ViewPager2.OnPageChangeCallback listener) {
        this.changeCallback = listener;
        return this;
    }

    public BannerView setOffscreenPageLimit(int limit) {
        viewPager2.setOffscreenPageLimit(limit);
        return this;
    }

    /**
     * 设置viewpager2的切换时长
     */
    public BannerView setPagerScrollDuration(long pagerScrollDuration) {
        this.pagerScrollDuration = pagerScrollDuration;
        return this;
    }

    /**
     * 设置轮播方向
     *
     * @param orientation Orientation.ORIENTATION_HORIZONTAL or default
     *                    Orientation.ORIENTATION_VERTICAL
     */
    public BannerView setOrientation(@ViewPager2.Orientation int orientation) {
        viewPager2.setOrientation(orientation);
        return this;
    }

    public BannerView addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        viewPager2.addItemDecoration(decor);
        return this;
    }

    public BannerView addItemDecoration(@NonNull RecyclerView.ItemDecoration decor, int index) {
        viewPager2.addItemDecoration(decor, index);
        return this;
    }

    /**
     * 是否自动轮播 大于1页轮播才生效
     */
    public BannerView setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
        if (isAutoPlay && getRealCount() > 1) {
            startTurning();
        }
        return this;
    }

    public boolean isAutoPlay() {
        return isAutoPlay && getRealCount() > 1;
    }

    public BannerView setIndicator(Indicator indicator) {
        return setIndicator(indicator, true);
    }

    /**
     * 设置indicator，支持在xml中创建
     *
     * @param attachToRoot true 添加到banner布局中
     */
    public BannerView setIndicator(Indicator indicator, boolean attachToRoot) {
        if (this.indicator != null) {
            removeView(this.indicator.getView());
        }
        if (indicator != null) {
            this.indicator = indicator;
            if (attachToRoot) {
                addView(this.indicator.getView(), this.indicator.getParams());
            }
        }
        return this;
    }

    /**
     * 设置banner圆角 api21以上
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerView setRoundCorners(final float radius) {
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        setClipToOutline(true);
        return this;
    }

    public void setCurrentItem(int position) {
        setCurrentItem(position, true);
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        tempPosition = position + sidePage;
        viewPager2.setCurrentItem(tempPosition, smoothScroll);
    }

    /**
     * 返回真实位置
     */
    public int getCurrentPager() {
        int position = toRealPosition(tempPosition);
        return Math.max(position, 0);
    }

    public ViewPager2 getViewPager2() {
        return viewPager2;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapterWrapper.adapter;
    }

    public void startTurning() {
        stopTurning();
        postDelayed(task, autoTurningTime);
        isTaskPostDelayed = true;
    }

    public void stopTurning() {
        if (isTaskPostDelayed) {
            removeCallbacks(task);
            isTaskPostDelayed = false;
        }
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        setAdapter(adapter, 0);
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter, int startPosition) {
        adapterWrapper.registerAdapter(adapter);
        startPager(startPosition);
    }

    /**
     * 不可见时停止自动轮播
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startTurning();
        } else {
            stopTurning();
        }
    }
}
