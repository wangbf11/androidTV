package com.example.xueliang.activity;

import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.MonitorPresenter;
import com.example.xueliang.utils.DialogUtil;

public class MonitorActivity extends BaseMvpActivity<MonitorPresenter> implements LoadCallBack {

    private TextView mTv_time;
    private LinearLayout ll_abnormal;

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
        ll_abnormal = findViewById(R.id.ll_abnormal);


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
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:   //向上键

                break;
            case KeyEvent.KEYCODE_0:   //数字键0

                break;
            case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //打开左边位置选中栏目
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
}
