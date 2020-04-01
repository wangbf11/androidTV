package com.example.xueliang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.xueliang.R;

/*
 * 登录页面
 */
public class LoginActivity extends Activity {

    private ImageView mQrCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mQrCode = findViewById(R.id.iv_qrcode);
        initView();
    }

    private void initView() {
        mQrCode.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
