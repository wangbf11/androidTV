package com.example.xueliang.manager;

import com.example.xueliang.utils.AppUtils;

import java.util.ArrayList;

import tv.danmaku.ijk.media.player.widget.IjkVideoView;

public class VideoManager {
    private static VideoManager sInstance;
    private ArrayList<IjkVideoView> mVideoViewArrayList = new ArrayList<>();//最大四个


    public static VideoManager getInstance() {
        if (sInstance == null) {
            synchronized (VideoManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoManager();
                }
            }
        }
        return sInstance;
    }

    private VideoManager() {
        init();
    }

    /**
     * 上传文件
     */
    public void init() {
        IjkVideoView ijkVideoView1 = new IjkVideoView(AppUtils.getApplication());
        IjkVideoView ijkVideoView2 = new IjkVideoView(AppUtils.getApplication());
        IjkVideoView ijkVideoView3 = new IjkVideoView(AppUtils.getApplication());
        IjkVideoView ijkVideoView4 = new IjkVideoView(AppUtils.getApplication());
        mVideoViewArrayList.add(ijkVideoView1);
        mVideoViewArrayList.add(ijkVideoView2);
        mVideoViewArrayList.add(ijkVideoView3);
        mVideoViewArrayList.add(ijkVideoView4);
    }

    public ArrayList<IjkVideoView> getVideoViewArrayList() {
        return mVideoViewArrayList;
    }

    public void onPause() {
        for (IjkVideoView item :mVideoViewArrayList){
            item.pause();
        }

    }
}
