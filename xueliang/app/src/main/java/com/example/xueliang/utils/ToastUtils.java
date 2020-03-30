package com.example.xueliang.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by wbf on 2019/1/7
 */
public class ToastUtils {

    public static void toast(String content) {
        Toast.makeText(AppUtils.getApplication(), content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 是否打开Toast显示开关
     */
    private static boolean isShow = true;

    private static Toast sToast;

    /**
     * 最常用的提示文本
     */
    public static void show(String message) {
        if (isShow) {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
            sToast = Toast.makeText(AppUtils.getApplication().getApplicationContext(), message, Toast.LENGTH_SHORT);
            sToast.show();
        }
    }


    /**
     * 直接显示文本
     *
     * @param messageId 需要显示的文字
     */
    public static void showShort(int messageId) {
        if (isShow) {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
            sToast = Toast.makeText(AppUtils.getApplication(), messageId, Toast.LENGTH_SHORT);
            sToast.show();
        }
    }

    /**
     * 直接显示文本
     *
     * @param message 需要显示的文字
     */
    public static void showShort(String message) {
        if (isShow) {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
            sToast = Toast.makeText(AppUtils.getApplication(), message, Toast.LENGTH_SHORT);
            sToast.show();
        }
    }

    /**
     * 直接显示文本
     *
     * @param messageId 需要显示的文字
     */
    public static void showLong(int messageId) {
        if (isShow) {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
            sToast = Toast.makeText(AppUtils.getApplication(), messageId, Toast.LENGTH_LONG);
            sToast.show();
        }
    }

    /**
     * 直接显示文本
     *
     * @param message 需要显示的文字
     */
    public static void showLong(String message) {
        if (isShow) {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
            sToast = Toast.makeText(AppUtils.getApplication(), message, Toast.LENGTH_LONG);
            sToast.show();
        }
    }

    /**
     * 直接显示文本
     *
     * @param messageId 需要显示的文字资源
     * @param duration  自定义显示时间
     */
    public static void show(int messageId, int duration) {
        if (isShow) {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
            sToast = Toast.makeText(AppUtils.getApplication(), messageId, duration);
            sToast.show();
        }
    }

    /**
     * 直接显示文本
     *
     * @param message  需要显示的文字
     * @param duration 自定义显示时间
     */
    public static void show(String message, int duration) {
        if (isShow) {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
            sToast = Toast.makeText(AppUtils.getApplication(), message, duration);
            sToast.show();
        }
    }

    /**
     * 带图片消息提示
     *
     * @param ImageResourceId 图片资源
     * @param messageId       文字资源
     */
    public static void showImageAndText(int ImageResourceId, int messageId) {
        Context context = AppUtils.getApplication();
        showImageAndText(ImageResourceId, context.getResources().getString(messageId), Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * 带图片消息提示
     *
     * @param ImageResourceId 图片资源
     * @param message         文字
     */
    public static void showImageAndText(int ImageResourceId, CharSequence message) {
        showImageAndText(ImageResourceId, message, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * 带图片消息提示
     *
     * @param ImageResourceId 图片资源
     * @param message         文字
     * @param duration        显示时间
     * @param gravity         显示位置
     */
    public static void showImageAndText(int ImageResourceId, CharSequence message, int duration, int gravity) {
        Toast toast = Toast.makeText(AppUtils.getApplication(),
                message, duration);
        toast.setGravity(gravity, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(AppUtils.getApplication());
        imageCodeProject.setImageResource(ImageResourceId);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }
}
