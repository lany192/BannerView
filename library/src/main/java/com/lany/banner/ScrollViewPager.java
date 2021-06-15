package com.lany.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Keep;
import androidx.viewpager.widget.ViewPager;

@Keep
public class ScrollViewPager extends ViewPager {
    private boolean scrollable = true;

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.scrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.scrollable && super.onInterceptTouchEvent(ev);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
