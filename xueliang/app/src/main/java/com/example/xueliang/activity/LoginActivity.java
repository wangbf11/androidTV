package com.example.xueliang.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.LoginPresenter;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.QRCodeUtil;
import com.example.xueliang.utils.SPUtil;
import com.example.xueliang.utils.StringUtils;

/*
 * 登录页面
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoadCallBack {

    private ImageView mQrCode;

    @Override
    public LoginPresenter setPresenter() {
        return new LoginPresenter(this);
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


    }

    @Override
    public void initListener() {
        mQrCode.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void loadData() {
        if (StringUtils.isNotBlank(SPUtil.getToken())){
            onLoginSuccess();
        }else {
            presenter.getLoginQrCode();
        }
    }

    @Override
    public void onLoad(Object data) {

    }

    @Override
    public void onLoadFail(String message) {

    }

    public void onGetQrCode(String message) {
        Bitmap cdd = QRCodeUtil.syncEncodeQRCode(message, AppUtils.dip2px(158));
        Glide.with(this)
                .load(cdd)
                .into(mQrCode);
    }

    public void onLoginSuccess() {
        //登录成功跳转首页
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
