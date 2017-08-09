

package com.lany.bannerview.transformer;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public abstract class ABaseTransformer implements PageTransformer {


    protected abstract void onTransform(View page, float position);


    @Override
    public void transformPage(View page, float position) {
        onPreTransform(page, position);
        onTransform(page, position);
        onPostTransform(page, position);
    }


    protected boolean hideOffscreenPages() {
        return true;
    }


    protected boolean isPagingEnabled() {
        return false;
    }



    protected void onPreTransform(View page, float position) {
        final float width = page.getWidth();

        page.setRotationX(0);
        page.setRotationY(0);
        page.setRotation(0);
        page.setScaleX(1);
        page.setScaleY(1);
        page.setPivotX(0);
        page.setPivotY(0);
        page.setTranslationY(0);
        page.setTranslationX(isPagingEnabled() ? 0f : -width * position);

        if (hideOffscreenPages()) {
            page.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
//			page.setEnabled(false);
        } else {
//			page.setEnabled(true);
            page.setAlpha(1f);
        }
    }


    protected void onPostTransform(View page, float position) {
    }


    protected static final float min(float val, float min) {
        return val < min ? min : val;
    }

}
