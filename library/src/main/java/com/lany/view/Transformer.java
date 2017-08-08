package com.lany.view;

import android.support.v4.view.ViewPager.PageTransformer;

import com.lany.view.transformer.AccordionTransformer;
import com.lany.view.transformer.BackgroundToForegroundTransformer;
import com.lany.view.transformer.CubeInTransformer;
import com.lany.view.transformer.CubeOutTransformer;
import com.lany.view.transformer.DefaultTransformer;
import com.lany.view.transformer.DepthPageTransformer;
import com.lany.view.transformer.FlipHorizontalTransformer;
import com.lany.view.transformer.FlipVerticalTransformer;
import com.lany.view.transformer.ForegroundToBackgroundTransformer;
import com.lany.view.transformer.RotateDownTransformer;
import com.lany.view.transformer.RotateUpTransformer;
import com.lany.view.transformer.ScaleInOutTransformer;
import com.lany.view.transformer.StackTransformer;
import com.lany.view.transformer.TabletTransformer;
import com.lany.view.transformer.ZoomInTransformer;
import com.lany.view.transformer.ZoomOutSlideTransformer;
import com.lany.view.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
