package com.example.xueliang.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.presenter.MonitorPresenter;
import com.example.xueliang.utils.DialogUtil;
import com.example.xueliang.utils.PointUtils;
import com.example.xueliang.utils.ProgressHUD;
import com.example.xueliang.utils.StringUtils;
import com.example.xueliang.utils.ToastUtils;
import com.example.xueliang.view.MonitorPickerDialog;
import com.example.xueliang.view.listener.OnPickListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.widget.IjkVideoView;

public class MonitorActivity extends BaseMvpActivity<MonitorPresenter> implements LoadCallBack<List<TownBean>> {
    public static final String POINT_BEAN = "point_bean";
    private TextView mTv_time;
    private TextView tv_location;
    private LinearLayout ll_tips;


    private PointBean mPointBean;
    private TextView town_cun_name;
    private TextView point_name;
    private IjkVideoView mVvPlayer;
    private MonitorPickerDialog mCountryPickerDialog;
    private Dialog mExceptionDialog;
    private KProgressHUD mKProgressHUD;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPointBean = (PointBean) getIntent().getSerializableExtra(POINT_BEAN);
    }

    @Override
    public void initView() {
        mTv_time = findViewById(R.id.tv_time);
        ll_tips = findViewById(R.id.ll_tips);
        tv_location = findViewById(R.id.tv_location);
        town_cun_name = findViewById(R.id.town_cun_name);
        point_name = findViewById(R.id.point_name);

        mVvPlayer = findViewById(R.id.surfaceView1);

        createCountDownTimer();

        String url = PointUtils.getPlayerUrl(mPointBean);
        mVvPlayer.setVideoPath(url);
        mVvPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                if(null != mKProgressHUD){
                    mKProgressHUD.dismiss();
                }
                mVvPlayer.start();
            }
        });


        mVvPlayer.setOnErrorListener((mp, what, extra) -> {
            // 缓存有问题 先删除 缓存
            mVvPlayer.stopPlayback();
            return true;
        });

        updateUI();

        mCountryPickerDialog = new MonitorPickerDialog(this);
        mCountryPickerDialog.setOnPickListener(new OnPickListener() {
            @Override
            public void onPick(int position, PointBean data, Dialog dialog) {
                mPointBean = data;
                onClickChange();
                dialog.dismiss();
            }

            @Override
            public void onCancel() {
                mCountryPickerDialog.dismiss();
            }
        });


        mExceptionDialog = DialogUtil.CreateAlertException(mContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which2) {
                mExceptionDialog.dismiss();
                String id = mPointBean.getId();
                if (StringUtils.isNotEmpty(id)) {
                    //上报异常
                    DialogUtil.showAlert(mContext, null, "您确认要上报异常情况吗？",
                            "确 定", (dialog, which) -> {
                                presenter.oneKeyQZ(mPointBean.getId());
                                dialog.dismiss();
                            }, "取 消", (dialog, which) -> {
                                dialog.dismiss();
                            }, false);
                } else {
                    ToastUtils.show("上报的Id为空不能上报!");
                }
            }
        }, false);

    }

    public void onClickChange() {
        mKProgressHUD = ProgressHUD.show(this);
        Map<String, Object> params = new HashMap<>();
        params.put("id", mPointBean.getId());
        RetrofitManager.getDefault().getPointListByPointId(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<PointBean>() {
                    @Override
                    protected void onSucess(PointBean point) {
                        mPointBean.setRtmpSrc(point.getRtmpSrc());
                        mPointBean.setRtspSrc(point.getRtspSrc());
                        mPointBean.setEquipment_num(point.getEquipment_num());
                        String url = PointUtils.getPlayerUrl(mPointBean);
                        String videoPath = mVvPlayer.getVideoPath();
                        if (!url.equals(videoPath)){
                            mVvPlayer.stopPlayback();
                            mVvPlayer.setVideoPath(url);
                            mVvPlayer.start();
                        }
                        updateUI();
                    }

                    @Override
                    protected void onFail(String err) {
                        mKProgressHUD.dismiss();
                        Log.e("err", "err");
                        ToastUtils.show("切换视频失败");
                    }
                });

    }

    private void updateUI() {
        mTv_time.setText(mPointBean.getEquipment_num());
        String location = mPointBean.getLocation();
        String town = mPointBean.getTown();

        String village = mPointBean.getVillage();
        String id = mPointBean.getId();
        if (StringUtils.isEmpty(village)) {
            village = "";
        }
        if (StringUtils.isEmpty(town)) {
            town = "";
        }
        if (StringUtils.isEmpty(location)) {
            location = "";
        }
        if (StringUtils.isEmpty(id)) {
            id = "";
        }
        tv_location.setText(town + " " + village + " " + location);

        point_name.setText(location);
        town_cun_name.setText(town + " " + village + "   编号：" + id);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void loadData() {
        presenter.processLogic();
    }

    @Override
    public void onLoad(List<TownBean> data) {
    }

    @Override
    public void onLoadFail(String message) {

    }

    /**
     * 求助成功
     *
     * @param message
     */
    public void onQZSucess(String message) {

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
                if (event.getAction() == KeyEvent.ACTION_DOWN && !mCountryPickerDialog.isShowing() &&!mExceptionDialog.isShowing()) {
                    //打开异常提交栏目
                    mExceptionDialog.show();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:   //向上键

                break;
            case KeyEvent.KEYCODE_0:   //数字键0

                break;
            case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
                if (event.getAction() == KeyEvent.ACTION_DOWN && !mCountryPickerDialog.isShowing() &&!mExceptionDialog.isShowing()) {
                    //打开左边位置选中栏目
                    mCountryPickerDialog.show();
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
