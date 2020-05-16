package com.example.xueliang.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.adapter.BaseQuickAdapter;
import com.example.xueliang.adapter.NavMonitorPageCunListAdapter;
import com.example.xueliang.adapter.NavMonitorPagePointListAdapter;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.bean.VillageBean;
import com.example.xueliang.presenter.MonitorPresenter;
import com.example.xueliang.utils.DialogUtil;
import com.example.xueliang.utils.PointUtils;
import com.example.xueliang.utils.StringUtils;
import com.example.xueliang.utils.ToastUtils;
import com.yan.tvprojectutils.FocusRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.widget.IjkVideoView;

public class MonitorActivity extends BaseMvpActivity<MonitorPresenter> implements LoadCallBack<List<TownBean>>  {
    public static final String POINT_BEAN = "point_bean";
    private TextView mTv_time;
    private TextView tv_location;
    private LinearLayout ll_abnormal;
    private LinearLayout ll_abnormal_click;
    private LinearLayout ll_left_list;
    private LinearLayout ll_tips;
    private boolean mIsBottomHide = false;
    private boolean mIsLeftHide = false;
    private FocusRecyclerView mRv_cun;
    private FocusRecyclerView mRv_monitor;
    private NavMonitorPageCunListAdapter cunAdapter;
    private NavMonitorPagePointListAdapter pointAdapter;
    private List<VillageBean> cunList = new ArrayList<>();
    private List<PointBean> monitorList = new ArrayList<>();
    private ImageView iv_left_close;
    private PointBean mPointBean;
    private TextView town_cun_name;
    private TextView point_name;
    private IjkVideoView mVvPlayer;

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
        mPointBean = (PointBean)getIntent().getSerializableExtra(POINT_BEAN);
    }

    @Override
    public void initView() {
        mTv_time = findViewById(R.id.tv_time);
        ll_tips = findViewById(R.id.ll_tips);
        tv_location = findViewById(R.id.tv_location);
        town_cun_name = findViewById(R.id.town_cun_name);
        point_name = findViewById(R.id.point_name);
        ll_abnormal = findViewById(R.id.ll_abnormal);
        ll_abnormal_click = findViewById(R.id.ll_abnormal_click);
        ll_left_list = findViewById(R.id.ll_left_list);
        iv_left_close = findViewById(R.id.iv_left_close);
        mVvPlayer = findViewById(R.id.surfaceView1);




        ll_abnormal.setVisibility(View.GONE);
        ll_left_list.setVisibility(View.GONE);
        mIsBottomHide = true;
        mIsLeftHide = true;


        mRv_cun = (FocusRecyclerView) findViewById(R.id.rv_cun_data);
        mRv_monitor = (FocusRecyclerView) findViewById(R.id.rv_monitor_data);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv_cun.setLayoutManager(mLayoutManager);
        mRv_cun.setHasFixedSize(true);
        mRv_cun.setAdapter(cunAdapter = new NavMonitorPageCunListAdapter(this, cunList));
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv_monitor.setLayoutManager(mLayoutManager2);
        mRv_monitor.setHasFixedSize(true);
        mRv_monitor.setAdapter(pointAdapter = new NavMonitorPagePointListAdapter(this, monitorList));

        createCountDownTimer();

        String url = PointUtils.getPlayerUrl(mPointBean);
        mVvPlayer.setVideoPath(url);
        mVvPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener()  {

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                mVvPlayer.start();
            }
        });


        mVvPlayer.setOnErrorListener((mp, what, extra) -> {
            // 缓存有问题 先删除 缓存
            mVvPlayer.stopPlayback();
            return true;
        });

        updateUI();
    }

    private void updateUI() {
        mTv_time.setText(mPointBean.getEquipment_num());
        String location = mPointBean.getLocation();
        String town = mPointBean.getTown();

        String village = mPointBean.getVillage();
        String equipment_num = mPointBean.getEquipment_num();
        if (StringUtils.isEmpty(village)) {
            village = "";
        }
        if (StringUtils.isEmpty(town)) {
            town = "";
        }
        if (StringUtils.isEmpty(location)) {
            location = "";
        }
        if (StringUtils.isEmpty(equipment_num)) {
            equipment_num = "";
        }
        tv_location.setText(town + " " + village + " " + location);

        point_name.setText(location);
        town_cun_name.setText(town + " " + village + "   编号：" + equipment_num);
    }

    @Override
    public void initListener() {
        ll_abnormal_click.setOnClickListener(v -> {
            if (mPointBean.getEquipment_num() != null) {
                //上报异常
                DialogUtil.showAlert(mContext, null, "您确认要上报异常情况吗？",
                        "确 定", (dialog, which) -> {
                            presenter.oneKeyQZ(mPointBean.getEquipment_num());
                            dialog.dismiss();
                        }, "取 消", (dialog, which) -> {
                            dialog.dismiss();
                        }, false);
            }else{
                ToastUtils.show("上报的equipment_num为空不能上报！！");
            }
        });

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
                mPointBean = monitorList.get(position);
                updateUI();
                String url = PointUtils.getPlayerUrl(mPointBean);
                String videoPath = mVvPlayer.getVideoPath();
                if (!url.equals(videoPath)){
                    mVvPlayer.setVideoPath(url);
                }
            }
        });

        iv_left_close.setOnClickListener(v->{
            mIsLeftHide = false;
            openList();
        });
        iv_left_close.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(getResources().getDrawable(R.drawable.bg_boder));
                } else {
                    v.setBackground(null);
                }
            }

        });
    }

    @Override
    public void loadData() {
        presenter.processLogic();
    }

    @Override
    public void onLoad(List<TownBean> data) {
        cunList.clear();
        cunList.addAll(data.get(0).getChild());
        cunAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFail(String message) {

    }

    /**
     * 求助成功
     * @param message
     */
    public void onQZSucess(String message) {
        if (!mIsBottomHide){
            openError();
        }
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
                if (event.getAction() == KeyEvent.ACTION_DOWN && mIsLeftHide) {
                    //打开异常提交栏目
                    openError();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:   //向上键

                break;
            case KeyEvent.KEYCODE_0:   //数字键0

                break;
            case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
                if (event.getAction() == KeyEvent.ACTION_DOWN && mIsBottomHide && mIsLeftHide) {
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
            ll_abnormal.setVisibility(View.VISIBLE);
            ll_abnormal_click.requestFocus();
        } else {
            ll_abnormal.setVisibility(View.GONE);
        }

        mIsBottomHide = !mIsBottomHide;
    }

    /**
     * 打开和隐藏左边栏目
     */
    public void openList() {
        //图片点击事件 上下 toolbar 动画
        if (mIsLeftHide) {
            ll_left_list.setVisibility(View.VISIBLE);
        } else {
            ll_left_list.setVisibility(View.GONE);
        }

        mIsLeftHide = !mIsLeftHide;
    }

    /**
     * 2秒后消失提示
     */
    private void createCountDownTimer() {
        AlphaAnimation a = new AlphaAnimation(1, 0);
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

    @Override
    protected void onResume() {
        super.onResume();
        mVvPlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVvPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVvPlayer.stopPlayback();
        mVvPlayer.release(true);
        mVvPlayer.seekTo(0);
    }
}
