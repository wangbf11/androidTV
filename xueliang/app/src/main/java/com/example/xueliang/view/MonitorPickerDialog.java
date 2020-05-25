package com.example.xueliang.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.xueliang.R;
import com.example.xueliang.adapter.BaseQuickAdapter;
import com.example.xueliang.adapter.NavMonitorPageCunListAdapter;
import com.example.xueliang.adapter.NavMonitorPagePointListAdapter;
import com.example.xueliang.bean.CommonResult2;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.bean.VillageBean;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.view.listener.OnPickListener;
import com.yan.tvprojectutils.FocusRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * Created by wbf
 */

public class MonitorPickerDialog extends Dialog {
    private Context mContext;
    private Activity mActivity;
    private OnPickListener mOnPickListener;
    private FocusRecyclerView mRv_cun;
    private FocusRecyclerView mRv_monitor;
    private NavMonitorPageCunListAdapter cunAdapter;
    private NavMonitorPagePointListAdapter pointAdapter;
    private List<VillageBean> cunList = new ArrayList<>();
    private List<PointBean> monitorList = new ArrayList<>();
    private ImageView iv_left_close;

    public MonitorPickerDialog(@NonNull Activity activity) {
        super(activity, R.style.Dialog2);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_monitor_list_layout);
        mContext = getContext();
        setUpWindow();
        initData();
        initWidget();
        initClick();
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.LEFT;
        window.setAttributes(lp);
    }

    private void initData() {

    }

    private void initWidget() {
        iv_left_close = findViewById(R.id.iv_left_close);
        mRv_cun = (FocusRecyclerView) findViewById(R.id.rv_cun_data);
        mRv_monitor = (FocusRecyclerView) findViewById(R.id.rv_monitor_data);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv_cun.setLayoutManager(mLayoutManager);
        mRv_cun.setHasFixedSize(true);
        mRv_cun.setAdapter(cunAdapter = new NavMonitorPageCunListAdapter(mActivity, cunList));
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv_monitor.setLayoutManager(mLayoutManager2);
        mRv_monitor.setHasFixedSize(true);
        mRv_monitor.setAdapter(pointAdapter = new NavMonitorPagePointListAdapter(mActivity, monitorList));
        geList(); //获取数据
    }

    private void initClick() {
        cunAdapter.setOnItemChildFocusChangeListener(new BaseQuickAdapter.OnItemChildFocusChangeListener() {
            @Override
            public void onFocusChange(BaseQuickAdapter adapter, View v, boolean hasFocus, int position) {
                if (hasFocus) {
                    monitorList.clear();
                    pointAdapter.notifyDataSetChanged();
                    VillageBean villageBean = cunList.get(position);
                    monitorList.clear();
                    monitorList.addAll(villageBean.getChild());
                    pointAdapter.notifyDataSetChanged();
                }

            }
        });

        cunAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                monitorList.clear();
                pointAdapter.notifyDataSetChanged();
                VillageBean villageBean = cunList.get(position);
                monitorList.clear();
                monitorList.addAll(villageBean.getChild());
                pointAdapter.notifyDataSetChanged();
            }
        });

        pointAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (null != mOnPickListener){
                    mOnPickListener.onPick(position,monitorList.get(position),MonitorPickerDialog.this);
                }
            }
        });


        iv_left_close.setOnClickListener(v->{
            if (null != mOnPickListener){
                mOnPickListener.onCancel();
            }
        });


        iv_left_close.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_boder));
                } else {
                    v.setBackground(null);
                }
            }

        });

    }

    public void geList() {
        Map<String, Object> params = new HashMap<>();
        RetrofitManager.getDefault().getTownAndCunList(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<CommonResult2<List<TownBean>>>() {
                    @Override
                    protected void onSucess(CommonResult2<List<TownBean>> data) {
                        if (null == data){
                            return;
                        }
                        List<TownBean> list = data.getResult();
                        if (list != null &&list.size() >0) {
                            cunList.clear();
                            cunList.addAll(list.get(0).getChild());
                            cunAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    protected void onFail(String err) {
                    }
                });
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setOnPickListener(OnPickListener listener) {
        this.mOnPickListener = listener;
    }
}
