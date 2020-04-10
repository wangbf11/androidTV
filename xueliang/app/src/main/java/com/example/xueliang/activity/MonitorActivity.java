package com.example.xueliang.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.adapter.NavCunAdapter;
import com.example.xueliang.adapter.NavMonitorAdapter;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.MonitorPresenter;
import com.example.xueliang.utils.DialogUtil;
import com.yan.tvprojectutils.FocusRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MonitorActivity extends BaseMvpActivity<MonitorPresenter> implements LoadCallBack {

    private TextView mTv_time;
    private LinearLayout ll_abnormal;
    private LinearLayout ll_left_list;
    private LinearLayout ll_tips;
    private boolean mIsBottomHide =false;
    private boolean mIsLeftHide = false;
    private FocusRecyclerView mRv_cun;
    private FocusRecyclerView mRv_monitor;
    private NavCunAdapter cunAdapter;
    private NavMonitorAdapter monitorAdapter;
    private List<String> cunList = new ArrayList<>();
    private List<String> monitorList = new ArrayList<>();


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
        ll_tips = findViewById(R.id.ll_tips);
        ll_abnormal = findViewById(R.id.ll_abnormal);
        ll_left_list = findViewById(R.id.ll_left_list);
        mTv_time.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_abnormal.setTranslationY(ll_abnormal.getMeasuredHeight());
                ll_left_list.setTranslationX(-ll_left_list.getMeasuredHeight());
                mIsBottomHide =true;
                mIsLeftHide = true;
            }
        },0);


        mRv_cun = (FocusRecyclerView)findViewById(R.id.rv_cun_data);
        mRv_monitor = (FocusRecyclerView)findViewById(R.id.rv_monitor_data);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv_cun.setLayoutManager(mLayoutManager);
        mRv_cun.setHasFixedSize(true);
        mRv_cun.setAdapter(cunAdapter = new NavCunAdapter(this, cunList));
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv_monitor.setLayoutManager(mLayoutManager2);
        mRv_monitor.setHasFixedSize(true);
        mRv_monitor.setAdapter(monitorAdapter = new NavMonitorAdapter(this, monitorList));

        createCountDownTimer();
    }

    @Override
    public void initListener() {
        ll_abnormal.setOnClickListener(v -> {
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
    public void loadData() {
        cunList.add("SDFASDFSDFADFSDAF");
        cunList.add("测试测试测试测试测试测试测试测试");
        cunList.add("测试测试测试测试");
        cunList.add("测试");
        cunAdapter.notifyDataSetChanged();


        monitorList.add("SDFASDFSDFADFSDAF");
        monitorList.add("测试测试测试测试测试测试测试测试");
        monitorList.add("测试测试测试测试");
        monitorList.add("测试");
        monitorList.add("SDFASDFSDFADFSDAF");
        monitorList.add("测试测试测试测试测试测试测试测试");
        monitorList.add("测试测试测试测试");
        monitorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoad(Object data) {

    }

    @Override
    public void onLoadFail(String message) {

    }

    /**
     * 实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
     * exp:KeyEvent.ACTION_UP
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:     //确定键enter
            case KeyEvent.KEYCODE_DPAD_CENTER:

                break;
            case KeyEvent.KEYCODE_BACK:    //返回键

                break;
            case KeyEvent.KEYCODE_SETTINGS: //设置键

                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:   //向下键
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //打开异常提交栏目
                    openError();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:   //向上键

                break;
            case KeyEvent.KEYCODE_0:   //数字键0

                break;
            case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //打开左边位置选中栏目
                    openList();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:  //向右键

                break;
            case KeyEvent.KEYCODE_INFO:    //info键

                break;
            case KeyEvent.KEYCODE_PAGE_DOWN:     //向上翻页键
            case KeyEvent.KEYCODE_MEDIA_NEXT:

                break;
            case KeyEvent.KEYCODE_PAGE_UP:     //向下翻页键
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:

                break;
            case KeyEvent.KEYCODE_VOLUME_UP:   //调大声音键

                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN: //降低声音键

                break;
            case KeyEvent.KEYCODE_VOLUME_MUTE: //禁用声音

                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);

    }


    /**
     * 打开和隐藏报警按钮
     */
    public void openError() {
        //图片点击事件 上下 toolbar 动画
        if (mIsBottomHide) {
            ll_abnormal.animate()
                    .translationYBy(-ll_abnormal.getMeasuredHeight())
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .start();
        } else {
            ll_abnormal.animate()
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .translationYBy(ll_abnormal.getMeasuredHeight())
                    .start();
        }

        mIsBottomHide = !mIsBottomHide;
    }

    /**
     * 打开和隐藏左边栏目
     */
    public void openList() {
        //图片点击事件 上下 toolbar 动画
        if (mIsLeftHide) {
            ll_left_list.animate()
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .translationXBy(ll_left_list.getMeasuredHeight())
                    .start();
        } else {
            ll_left_list.animate()
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .translationXBy(-ll_left_list.getMeasuredHeight())
                    .start();
        }

        mIsLeftHide = !mIsLeftHide;
    }

    /**
     * 2秒后消失提示
     */
    private void createCountDownTimer() {
        AlphaAnimation a = new AlphaAnimation(1,0);
        // 动画时长
        a.setDuration(4000);
        // 开启动画
        ll_tips.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_tips.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
