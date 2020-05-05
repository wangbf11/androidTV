package com.example.xueliang.activity;

import android.util.Log;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.adapter.NavGridMonitorAdapter;
import com.example.xueliang.adapter.NavTownListAdapter;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.network.ResponceSubscriber;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.presenter.MonitorListPresenter;
import com.example.xueliang.utils.ProgressHUD;
import com.example.xueliang.utils.ToastUtils;
import com.example.xueliang.view.listener.MyFocusChange;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yan.tvprojectutils.FocusRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MonitorListActivity extends BaseMvpActivity<MonitorListPresenter> implements LoadCallBack<List<TownBean>> {
    private List<TownBean> locationList = new ArrayList<>();
    private List<PointBean> gridList = new ArrayList<>();
    private List<PointBean> gridTempList = new ArrayList<>(); //1分屏幕和4分屏幕缓存  这个数组最大 4
    private NavTownListAdapter locationAdapter;
    private NavGridMonitorAdapter gridAdapter;
    private TextView mTv_one;
    private TextView mTv_four;
    private FocusRecyclerView mRv_grid;
    private FocusRecyclerView mRv_list;
    private KProgressHUD mKProgressHUD;

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
        mRv_grid.setAdapter(gridAdapter = new NavGridMonitorAdapter(this, gridList,true));

        mTv_one.setOnFocusChangeListener(new MyFocusChange());
        mTv_four.setOnFocusChangeListener(new MyFocusChange());
    }

    @Override
    public void initListener() {
        mTv_one.setOnClickListener(v->{
            //切换1分屏幕
            gridList.clear();
            gridList.add(gridTempList.get(0));
            GridLayoutManager focusGrid= new GridLayoutManager(getApplicationContext(), 1);
            mRv_grid.setLayoutManager(focusGrid);
            mRv_grid.setAdapter(gridAdapter = new NavGridMonitorAdapter(this, gridList,true));
            mTv_one.setSelected(true);
            mTv_four.setSelected(false);
        });

        mTv_four.setOnClickListener(v->{
            //切换四分屏
            mTv_four.setSelected(true);
            mTv_one.setSelected(false);
            gridList.clear();
            gridList.addAll(gridTempList);
            GridLayoutManager focusGrid= new GridLayoutManager(getApplicationContext(), 2);
            mRv_grid.setLayoutManager(focusGrid);
            mRv_grid.setAdapter(gridAdapter = new NavGridMonitorAdapter(this, gridList,false));
        });
    }

    @Override
    public void loadData() {
        mKProgressHUD = ProgressHUD.show(this);
        presenter.processLogic();
    }

    @Override
    public void onLoad(List<TownBean> data) {
        locationList.clear();
        locationList.addAll(data);
        locationAdapter.notifyDataSetChanged();


        Map<String, Object> params = new HashMap<>();
        params.put("id", data.get(0).getId());
        RetrofitManager.getDefault().getPointListByCunId(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List<PointBean>>() {
                    @Override
                    protected void onSucess(List<PointBean> points) {
                        mKProgressHUD.dismiss();
                        if (points != null && points.size() > 0) {
                            PointBean pointBean = points.get(0);
                            //默认第一个点作为监控点
                            gridTempList.clear();
                            gridList.clear();
                            gridList.add(pointBean);
                            gridTempList.add(pointBean);
                            gridAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("err", "err");
                        }
                    }

                    @Override
                    protected void onFail(String err) {
                        mKProgressHUD.dismiss();
                        Log.e("err", "err");
                    }
                });

    }

    @Override
    public void onLoadFail(String message) {
        mKProgressHUD.dismiss();
    }

    public void onPointItemChildClick(PointBean data) {

        if (mTv_one.isSelected()){
            gridList.clear();
            gridList.add(data);
        }else {
            if (gridList.contains(data)){
                ToastUtils.show("你添加了这个点位");
                return;
            }

            if (gridList.size() <4){
                gridList.add(data);
            }else {
                gridList.remove(0);
                gridList.add(data);
            }

            if (gridTempList.size() <4){
                gridTempList.add(data);
            }else {
                gridTempList.remove(0);
                gridTempList.add(data);
            }
        }
        gridAdapter.notifyDataSetChanged();
    }
}
