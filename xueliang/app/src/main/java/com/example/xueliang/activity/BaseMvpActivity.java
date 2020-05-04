package com.example.xueliang.activity;

import android.content.Context;
import android.os.Bundle;

import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.utils.AppUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;


public abstract class BaseMvpActivity<T extends BasePresenter> extends FragmentActivity {
    public Context mContext;

    public T presenter;

    public abstract T setPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.getApplication().addActivity(this);
        setContentView(getLayoutId());
        mContext = this;
        presenter = setPresenter();
        initData();
        initView();
        afterInitView();
        initListener();
        loadData();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.getApplication().removeActivity(this);
        if (null != presenter) {
            presenter.onDestroy();
            presenter = null;
        }
    }

    /**
     * 视图id
     */
    public abstract int getLayoutId();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 初始化视图后
     */
    protected  void afterInitView(){

    }

    /**
     * 初始化监听事件
     */
    public abstract void initListener();

    /**
     * 剩余的逻辑代码，加载数据都放在这里处理
     */
    public void loadData(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
