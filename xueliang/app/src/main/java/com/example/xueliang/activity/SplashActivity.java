package com.example.xueliang.activity;

import android.content.Intent;
import android.os.Handler;

import com.example.xueliang.R;
import com.example.xueliang.base.BasePresenter;

public class SplashActivity extends BaseMvpActivity {
    //跳转延迟1s时间
    private int TIME = 1000;

    @Override
    public BasePresenter setPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME);
    }

    @Override
    public void initListener() {

    }
}

