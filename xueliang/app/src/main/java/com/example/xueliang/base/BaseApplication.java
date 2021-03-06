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
        context = this;
        AppUtils.init(this);
    }

    public static Context getContext() {
        return context;
    }


    /**
     * 添加activity到LinkedList集合
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activitys.add(0,activity);
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activitys.remove(activity);
        }
    }

    public Activity getTopActivity(){
        return activitys.getFirst();
    }

    /**
     * 退出集合所有的activity
     */
    public void exit() {
        Activity activity;
        while (activitys.size()!=0) {
            activity=activitys.poll();
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
