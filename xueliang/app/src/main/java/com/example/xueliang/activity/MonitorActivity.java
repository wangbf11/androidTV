package com.example.xueliang.activity;

import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.MonitorPresenter;

public class MonitorActivity extends BaseMvpActivity<MonitorPresenter> implements LoadCallBack {
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

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onLoad(Object data) {

    }

    @Override
    public void onLoadFail(String message) {

    }
}
