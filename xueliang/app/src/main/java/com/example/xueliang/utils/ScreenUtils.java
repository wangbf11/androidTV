package com.example.xueliang.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by newbiechen on 17-5-1.
 */

public class ScreenUtils {

    public static int dpToPx(int dp){
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,metrics);
    }

    public static int pxToDp(int px){
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) (px / metrics.density);
    }

    public static int spToPx(int sp){
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,metrics);
    }

    public static int pxToSp(int px){
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) (px / metrics.scaledDensity);
    }

    /**
     * 获取手机显示App区域的大小（头部导航栏+ActionBar+根布局），不包括虚拟按钮
     * @return
     */
    public static int[] getAppSize(){
        int[] size = new int[2];
        DisplayMetrics metrics = getDisplayMetrics();
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

    /**
     * 获取整个手机屏幕的大小(包括虚拟按钮)
     * 必须在onWindowFocus方法之后使用
     * @param activity
     * @return
     */
    public static int[] getScreenSize(AppCompatActivity activity){
        int[] size = new int[2];
        View decorView = activity.getWindow().getDecorView();
        size[0] = decorView.getWidth();
        size[1] = decorView.getHeight();
        return size;
    }

    /**
     * 获取导航栏的高度
     * @return
     */
    public static int getStatusBarHeight(){
        Resources resources = AppUtils.getApplication().getResources();
        int resourceId = resources.getIdentifier("status_bar_height","dimen","android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private static final String NAVIGATION = "navigationBarBackground";


    public static DisplayMetrics getDisplayMetrics(){
        DisplayMetrics metrics = AppUtils.getApplication()
                .getResources()
                .getDisplayMetrics();
        return metrics;
    }
}
