package com.example.xueliang.activity;

import android.content.Intent;
import android.widget.ImageView;

import com.example.xueliang.R;
import com.example.xueliang.base.BasePresenter;

/*
 * 登录页面
 */
public class LoginActivity extends BaseMvpActivity {

    private ImageView mQrCode;

    @Override
    public BasePresenter setPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {

    }
    @Override
    public void initView() {
        mQrCode = findViewById(R.id.iv_qrcode);
        mQrCode.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void initListener() {

    }
}
