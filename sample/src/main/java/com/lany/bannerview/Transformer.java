package com.lany.bannerview;

import android.support.v4.view.ViewPager.PageTransformer;

import com.lany.bannerview.transformer.AccordionTransformer;
import com.lany.bannerview.transformer.BackgroundToForegroundTransformer;
import com.lany.bannerview.transformer.CubeInTransformer;
import com.lany.bannerview.transformer.CubeOutTransformer;
import com.lany.bannerview.transformer.DefaultTransformer;
import com.lany.bannerview.transformer.DepthPageTransformer;
import com.lany.bannerview.transformer.FlipHorizontalTransformer;
import com.lany.bannerview.transformer.FlipVerticalTransformer;
import com.lany.bannerview.transformer.ForegroundToBackgroundTransformer;
import com.lany.bannerview.transformer.RotateDownTransformer;
import com.lany.bannerview.transformer.RotateUpTransformer;
import com.lany.bannerview.transformer.ScaleInOutTransformer;
import com.lany.bannerview.transformer.StackTransformer;
import com.lany.bannerview.transformer.TabletTransformer;
import com.lany.bannerview.transformer.ZoomInTransformer;
import com.lany.bannerview.transformer.ZoomOutSlideTransformer;
import com.lany.bannerview.transformer.ZoomOutTransformer;

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
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTransformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
