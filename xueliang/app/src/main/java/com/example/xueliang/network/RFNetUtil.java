package com.example.xueliang.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.ToastUtils;


public class RFNetUtil {

    private RFNetUtil() {
    }

    public static boolean isNetworkConnected() {
        if (AppUtils.getApplication() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppUtils.getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static boolean checkWebNet(){
        if (!isNetworkConnected()){
            ToastUtils.show("请检查您的网络");
            return false;
        }else {
            return true;
        }
    }

    public static boolean isWifiConnected() {
        if (AppUtils.getApplication() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppUtils.getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected() {
        if (AppUtils.getApplication() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppUtils.getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType() {
        if (AppUtils.getApplication() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) AppUtils.getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /**
         移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         联通：130、131、132、152、155、156、185、186
         电信：133、153、180、189、（1349卫通）
         个别电话:170
         总结起来就是第一位必定为1，第二位必定为3或5或7或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (mobiles.equals("")) return false;
        else return mobiles.matches(telRegex);
    }
}
