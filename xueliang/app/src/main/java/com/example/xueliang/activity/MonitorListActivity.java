package com.example.xueliang.activity;

import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.MonitorListPresenter;

public class MonitorListActivity extends BaseMvpActivity<MonitorListPresenter> implements LoadCallBack {
    @Override
    public MonitorListPresenter setPresenter() {
        return new MonitorListPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_monitor_list;
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
