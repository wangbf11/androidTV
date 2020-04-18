package com.example.xueliang.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xueliang.R;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.QRCodeUtil;

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
        Bitmap cdd = QRCodeUtil.syncEncodeQRCode("syncEncodeQRCodesyncEncodeQRCodesyncEncodeQRCodesyncEncodeQRCodesyncEncodeQRCode", AppUtils.dip2px(158));
        Glide.with(this)
                .load(cdd)
                .into(mQrCode);

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
}
