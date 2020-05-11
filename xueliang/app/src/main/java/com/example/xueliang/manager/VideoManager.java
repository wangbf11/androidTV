package com.example.xueliang.manager;

import android.view.ViewGroup;
import android.view.ViewParent;

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

    /**
     * tag 是url
     * @param tag
     * @return
     */
    public IjkVideoView getVideoViewByTag(String tag) {
        IjkVideoView it = null;
        for (IjkVideoView videoView :mVideoViewArrayList){
                if (tag.equals(videoView.getTag())){
                    it = videoView;
                }
        }

        if (it == null){
            for (IjkVideoView videoView :mVideoViewArrayList){
                if (videoView.getTag() == null){
                    it = videoView;
                }
            }
        }else {
            ViewParent parent = it.getParent();
            if (null != parent){
                ViewGroup parent1 = (ViewGroup)it.getParent();
                parent1.removeAllViews();
            }
        }
        return it;
    }

    /**
     * tag 是url 移除的时候设置为null
     * @param tag
     * @return
     */
    public void setVideoViewTagToNull(String tag) {
        for (IjkVideoView videoView :mVideoViewArrayList){
            if (tag.equals(videoView.getTag())){
                videoView.setTag(null);
            }
        }
    }
}
