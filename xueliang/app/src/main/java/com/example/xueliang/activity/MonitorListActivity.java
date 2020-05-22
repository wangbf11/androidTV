package com.example.xueliang.activity;

import android.util.Log;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.adapter.NavGridMonitorAdapter;
import com.example.xueliang.adapter.NavTownListAdapter;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.manager.VideoManager;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.presenter.MonitorListPresenter;
import com.example.xueliang.utils.ProgressHUD;
import com.example.xueliang.utils.StringUtils;
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
            PointBean pointBean = gridList.get(0);
            gridTempList.clear();
            gridList.clear();
            gridList.add(pointBean);
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

            if(gridList.size() == 3){
                gridList.add(null);
            }

            if(gridList.size() == 2){
                gridList.add(null);
                gridList.add(null);
            }

            if(gridList.size() == 1){
                gridList.add(null);
                gridList.add(null);
                gridList.add(null);
            }
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


        PointBean pointBean = data.get(0).getChild().get(0).getChild().get(0);

        Map<String, Object> params = new HashMap<>();
        params.put("id", pointBean.getId());
        RetrofitManager.getDefault().getPointListByPointId(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<PointBean>() {
                    @Override
                    protected void onSucess(PointBean point) {
                        mKProgressHUD.dismiss();
                        if (point != null) {
                            if (point.getMsgState() == 1) {
                                point.setId(pointBean.getId());
                                //默认第一个点作为监控点
                                gridTempList.clear();
                                gridList.clear();
                                gridList.add(point);
                                gridAdapter.notifyDataSetChanged();
                            }else{
                                String msg = point.getMsg();
                                if (StringUtils.isNotEmpty(msg)) {
                                    ToastUtils.show(msg);
                                }
                            }
                        } else {
                            if (point.getMsgState() == 1) {
                                Log.e("err", "err");
                                gridTempList.clear();
                                gridList.clear();
                                gridList.add(pointBean);
                                gridAdapter.notifyDataSetChanged();
                            }else{
                                String msg = point.getMsg();
                                if (StringUtils.isNotEmpty(msg)) {
                                    ToastUtils.show(msg);
                                }
                            }
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

    public void onPointItemChildClick(PointBean pointBean) {
        List<PointBean> objects = new ArrayList<>();
        for (PointBean item :gridList){
            if (item != null){
                objects.add(item);
            }
            if (item != null && pointBean.getId().equals(item.getId())){
                ToastUtils.show("你已经添加了这个点位");
                return;
            }
        }

        VideoManager.getInstance().onPause();//每次添加都站停一下视频
        Map<String, Object> params = new HashMap<>();
        params.put("id", pointBean.getId());
        RetrofitManager.getDefault().getPointListByPointId(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<PointBean>() {
                    @Override
                    protected void onSucess(PointBean data) {
                        mKProgressHUD.dismiss();
                        if (data != null) {
                            if (data.getMsgState() != 1) {
                                String msg = data.getMsg();
                                if (StringUtils.isNotEmpty(msg)) {
                                    ToastUtils.show(msg);
                                }
                                return;
                            }
                            data.setId(pointBean.getId());
                            //默认第一个点作为监控点
                            if (mTv_one.isSelected()){
                                gridList.clear();
                                gridList.add(data);
                            }else {
                                if (objects.size() <4){
                                    objects.add(data);
                                    gridList.clear();
                                    gridList.addAll(objects);
                                }else {
                                    //重第一个 开始重新 覆盖
                                    if (gridTempList.size() <4){
                                        gridTempList.add(data);
                                        PointBean pointBean1 = gridList.get(gridTempList.size()-1);
                                        pointBean1.setId(data.getId());
                                        pointBean1.setRtmpSrc(data.getRtmpSrc());
                                        pointBean1.setRtspSrc(data.getRtspSrc());
                                        pointBean1.setEquipment_num(data.getEquipment_num());
                                        if (gridTempList.size() == 4){
                                            gridTempList.clear();
                                        }
                                    }
                                }


                                if(gridList.size() == 3){
                                    gridList.add(null);
                                }

                                if(gridList.size() == 2){
                                    gridList.add(null);
                                    gridList.add(null);
                                }

                                if(gridList.size() == 1){
                                    gridList.add(null);
                                    gridList.add(null);
                                    gridList.add(null);
                                }
                            }
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
    protected void onPause() {
        super.onPause();
        VideoManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoManager.getInstance().onResume();
    }
}
