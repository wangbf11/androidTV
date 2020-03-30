package com.example.xueliang.base;

import android.app.Activity;
import android.content.Context;


import com.example.xueliang.utils.AppUtils;

import java.util.LinkedList;

import androidx.multidex.MultiDexApplication;

/**
 * Created by apple on 2017/7/10.
 */

public class BaseApplication extends MultiDexApplication {

    private static Context context;
    public LinkedList<Activity> activitys = new LinkedList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
    }

    public static Context getContext() {
        return context;
    }

}
