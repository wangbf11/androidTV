package com.example.xueliang.activity;

import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.adapter.NavGridMonitorAdapter;
import com.example.xueliang.adapter.NavTownListAdapter;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.presenter.MonitorListPresenter;
import com.example.xueliang.view.listener.MyFocusChange;
import com.yan.tvprojectutils.FocusRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MonitorListActivity extends BaseMvpActivity<MonitorListPresenter> implements LoadCallBack<List<TownBean>> {
    private List<TownBean> locationList = new ArrayList<>();
    private List<String> gridList = new ArrayList<>();
    private NavTownListAdapter locationAdapter;
    private NavGridMonitorAdapter gridAdapter;
    private TextView mTv_one;
    private TextView mTv_four;
    private FocusRecyclerView mRv_grid;
    private FocusRecyclerView mRv_list;

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
        mTv_one = findViewById(R.id.tv_one);
        mTv_one.setSelected(true);
        mTv_four = findViewById(R.id.tv_four);


        mRv_list = findViewById(R.id.rv_list);
        mRv_grid = findViewById(R.id.rv_grid);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv_list.setLayoutManager(mLayoutManager);
        mRv_list.setHasFixedSize(true);
        mRv_list.setAdapter(locationAdapter = new NavTownListAdapter(this, locationList));


        GridLayoutManager focusGrid= new GridLayoutManager(getApplicationContext(), 1);
        mRv_grid.setLayoutManager(focusGrid);
        mRv_grid.setAdapter(gridAdapter = new NavGridMonitorAdapter(this, gridList));

        mTv_one.setOnFocusChangeListener(new MyFocusChange());
        mTv_four.setOnFocusChangeListener(new MyFocusChange());
    }

    @Override
    public void initListener() {
        mTv_one.setOnClickListener(v->{
            //切换1分屏幕
            gridList.clear();
            gridList.add("SDFASDFSDFADFSDAF");
            GridLayoutManager focusGrid= new GridLayoutManager(getApplicationContext(), 1);
            mRv_grid.setLayoutManager(focusGrid);
            mRv_grid.setAdapter(gridAdapter = new NavGridMonitorAdapter(this, gridList));
            mTv_one.setSelected(true);
            mTv_four.setSelected(false);
        });

        mTv_four.setOnClickListener(v->{
            //切换四分屏
            mTv_four.setSelected(true);
            mTv_one.setSelected(false);
            gridList.clear();
            gridList.add("SDFASDFSDFADFSDAF");
            gridList.add("SDFASDFSDFADFSDAF");
            gridList.add("SDFASDFSDFADFSDAF");
            gridList.add("SDFASDFSDFADFSDAF");
            GridLayoutManager focusGrid= new GridLayoutManager(getApplicationContext(), 2);
            mRv_grid.setLayoutManager(focusGrid);
            mRv_grid.setAdapter(gridAdapter = new NavGridMonitorAdapter(this, gridList));
        });
    }

    @Override
    public void loadData() {
        presenter.processLogic();
        gridList.add("SDFASDFSDFADFSDAF");
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoad(List<TownBean> data) {
        locationList.clear();
        locationList.addAll(data);
        locationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFail(String message) {

    }
}
