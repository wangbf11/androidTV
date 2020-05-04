package com.example.xueliang.manager;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LocationManager implements LocationListener {

    public boolean mbRequest = false;

    private static LocationManager mGnssManager = null;
    private static Context mContext = null;
    //高精度位置提供者标识
//    private static final String MOCK_LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    private static final String MOCK_LOCATION_PROVIDER = android.location.LocationManager.NETWORK_PROVIDER;

    private android.location.LocationManager mLocationManager;
    private final List<LocationListener> mListeners = new ArrayList<>();


    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static LocationManager getInstence(Context context) {
        if (mGnssManager == null && context != null) {
            mGnssManager = new LocationManager();
            mContext = context;
        }
        return mGnssManager;
    }

    /**
     * 注册位置服务监听
     *
     * @param listener
     */
    public void addListener(LocationListener listener) {
        synchronized (mListeners) {
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }
    }

    /**
     * 移除位置服务监听
     *
     * @param listener
     */
    public void removeListener(LocationListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    /**
     * 初始化位置服务事件
     */
    public void startLocation() {

        if (mContext == null) return;

        if (mbRequest) {
            return;
        }
        //注册GPS事件
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!requestLocationUpdates(mLocationManager, 2000, 0, this)) {
        }
    }


    /**
     * 重置位置服务
     *
     * @param mLocationManager
     * @param minTime
     * @param minDistance
     * @param listener
     * @return
     */

    @SuppressLint("MissingPermission")
    public boolean requestLocationUpdates(android.location.LocationManager mLocationManager,
                                          long minTime, float minDistance, LocationListener listener) {
        if (!isOpenGps(mContext)) {
            openGPS(mContext);
            return false;
        }
        if (mLocationManager.isProviderEnabled(MOCK_LOCATION_PROVIDER)) {
            mLocationManager.requestLocationUpdates(MOCK_LOCATION_PROVIDER,
                    minTime, minDistance, listener);
            mbRequest = true;
            return true;
        } else {
            return false;
            //GPS服务没有打开（请设置允许位置模拟,启动GPS服务程序,再试试）
        }
    }


    @Override
    public void onLocationChanged(Location arg0) {
        Log.e("ddddd", "ddddddd1");
        synchronized (mListeners) {
            for (LocationListener listener : mListeners) {
                listener.onLocationChanged(arg0);
            }
        }
    }

    @Override
    public void onProviderDisabled(String arg0) {

        mbRequest = false;
        startLocation();
        synchronized (mListeners) {
            for (LocationListener listener : mListeners) {
                listener.onProviderDisabled(arg0);
            }
        }
    }

    @Override
    public void onProviderEnabled(String arg0) {
        synchronized (mListeners) {
            for (LocationListener listener : mListeners) {
                listener.onProviderEnabled(arg0);
            }
        }
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        synchronized (mListeners) {
            for (LocationListener listener : mListeners) {
                listener.onStatusChanged(arg0, arg1, arg2);
            }
        }
    }

    //关闭gps定位
    public void stopLocation() {
        mbRequest = false;
        if (null != mLocationManager){
            mLocationManager.removeUpdates(this);
        }
    }

    /**
     * 判定本地定位是否开启
     * @param context
     * @return
     */
    public static boolean isOpenGps(Context context) {
        android.location.LocationManager alm = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


}
