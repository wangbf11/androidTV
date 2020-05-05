package com.example.xueliang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import hikvision.com.streamclient.GA_HIKPlayer;
import hikvision.com.streamclient.GA_HIKPlayerDelegate;
import hikvision.com.streamclient.GA_HIKPlayerUrlListener;

public class MVideoView extends SurfaceView {
    private GA_HIKPlayer hikPlayer;

    public MVideoView(Context context) {
        super(context);
        init();
    }

    public MVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public MVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                hikPlayer.setSurfaceViewHolder(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        hikPlayer = new GA_HIKPlayer(new GA_HIKPlayerUrlListener() {
            @Override
            public String getPlayUrl() {
                return "";//hik
            }
        });

        hikPlayer.setPlayerDelegate(new GA_HIKPlayerDelegate() {
            @Override
            public void didPlayFailed(GA_HIKPlayer ga_hikPlayer, Integer integer) {
                //播放失败了
            }

            @Override
            public void didReceivedMessage(GA_HIKPlayer ga_hikPlayer, Integer integer) {
                //播放成功了
            }

            @Override
            public void didReceivedDataLength(GA_HIKPlayer ga_hikPlayer, Integer integer) {

            }
        });
    }

    public void startRealPlayer(String url){
        hikPlayer.startRealPlayer(getContext(),url);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (hikPlayer != null) {
            hikPlayer.destoryPlayer();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
