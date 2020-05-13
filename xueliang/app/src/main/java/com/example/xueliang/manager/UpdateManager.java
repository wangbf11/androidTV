package com.example.xueliang.manager;

import android.content.Context;

public class UpdateManager {
    private static UpdateManager sInstance;
    private static Context mContext = null;

    public static UpdateManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (UpdateManager.class) {
                if (sInstance == null) {
                    sInstance = new UpdateManager();
                    mContext = context;
                }
            }
        }
        return sInstance;
    }

    private UpdateManager() {

    }



}
