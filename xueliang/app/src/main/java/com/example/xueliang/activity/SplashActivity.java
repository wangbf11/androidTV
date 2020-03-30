package com.example.xueliang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.xueliang.R;

public class SplashActivity  extends Activity {
    //跳转延迟1s时间
    private int TIME = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME);
    }
}

