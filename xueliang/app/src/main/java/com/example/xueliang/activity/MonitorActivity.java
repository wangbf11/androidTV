package com.example.xueliang.activity;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.MonitorPresenter;
import com.example.xueliang.utils.DialogUtil;

public class MonitorActivity extends BaseMvpActivity<MonitorPresenter> implements LoadCallBack {

    private TextView mTv_time;
    private LinearLayout ll_abnormal;

    @Override
    public MonitorPresenter setPresenter() {
        return new MonitorPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_monitor;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        mTv_time = findViewById(R.id.tv_time);
        ll_abnormal = findViewById(R.id.ll_abnormal);


    }

    @Override
    public void initListener() {
        ll_abnormal.setOnClickListener(v->{
            //上报异常
            DialogUtil.showAlert(mContext, null, "您确认要上报异常情况吗？",
                    "确 定", (dialog, which) -> {
                        dialog.dismiss();
                    }, "取 消", (dialog, which) -> {
                        dialog.dismiss();
                    }, false);
        });
    }

    @Override
    public void onLoad(Object data) {

    }

    @Override
    public void onLoadFail(String message) {

    }
}
