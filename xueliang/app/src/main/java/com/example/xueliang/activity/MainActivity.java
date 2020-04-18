package com.example.xueliang.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.MainPresenter;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.DialogUtil;
import com.example.xueliang.utils.ScreenUtils;
import com.example.xueliang.view.listener.MyFocusChange;
import com.example.xueliang.view.readview.PageLoader;
import com.example.xueliang.view.readview.PageView;
import com.example.xueliang.view.readview.TxtChapter;

import static androidx.core.view.ViewCompat.LAYER_TYPE_SOFTWARE;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends BaseMvpActivity<MainPresenter> implements LoadCallBack {
    PageView mPvPage;
    public PageLoader mPageLoader;
    public TextView mLogout;
    public LinearLayout dot_ll;
    public LinearLayout ll_monitor;
    public TextView mtvNotification;

    @Override
    public MainPresenter setPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void loadData() {
        presenter.processLogic();//调用接口
    }

    @Override
    public void initView() {
        mPvPage = findViewById(R.id.read_pv_page);
        mLogout = findViewById(R.id.tv_logout);
        ll_monitor = findViewById(R.id.ll_monitor);
        mtvNotification = findViewById(R.id.mtv_notification);
        dot_ll = (LinearLayout) findViewById(R.id.dot_ll);
        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mPageLoader = mPvPage.getPageLoader();
        ll_monitor.setOnFocusChangeListener(new MyFocusChange());
        mLogout.requestFocus();
    }

    @Override
    public void initListener() {
        mPageLoader.setPageStyle(); //设置默认颜色
        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return true;
            }

            @Override
            public void prePage() {
            }

            @Override
            public void nextPage() {
            }

            @Override
            public void cancel() {
            }
        });

        mPageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {
            @Override
            public void onPageCountChange(int count) {
                mPvPage.post(new Runnable() {
                    @Override
                    public void run() {
                        initDots(count);
                    }
                });

            }

            @Override
            public void onPageChange(int pos) {
                for (int i = 0; i < dot_ll.getChildCount(); i++) {
                    dot_ll.getChildAt(i).setBackgroundResource(pos == i ? R.drawable.icon_dot_focused : R.drawable.icon_dot_normal);
                }
            }
        });

        mLogout.setOnClickListener(v->{
            //退出登录
            DialogUtil.showAlert(mContext, null, "您确认要退出吗？",
                    "确 定", (dialog, which) -> {
                        dialog.dismiss();
                    }, "取 消", (dialog, which) -> {
                        dialog.dismiss();
                    }, false);
        });



        ll_monitor.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(this, MonitorListActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 初始化指示小圆点
     */
    private void initDots(int num) {
        dot_ll.removeAllViews();
        for (int i = 0; i < num; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( ScreenUtils.dpToPx(8),ScreenUtils.dpToPx(8));
            params.leftMargin = ScreenUtils.dpToPx(3);
            params.rightMargin = ScreenUtils.dpToPx(3);
            if (i == 0) {
                dot.setBackgroundResource(R.drawable.icon_dot_focused);
            } else {
                dot.setBackgroundResource(R.drawable.icon_dot_normal);
            }
            dot_ll.addView(dot, params);
        }
    }


    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if ( mPvPage.hasNextPage()){
                mPageLoader.skipToNextPage();
            }else {
                mPageLoader.skipToPage(0);
            }

            new Handler().postDelayed(runnable,5000);
        }
    };

    @Override
    public void onLoad(Object data) {

    }

    @Override
    public void onLoadFail(String message) {

    }


    public void onLoadNotice(String data) {
        TxtChapter txtChapter = new TxtChapter();
        txtChapter.setBody(data);
        mPageLoader.setChapter(txtChapter);

        mPvPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageLoader.parseCurChapter();
                new Handler().postDelayed(runnable,5000);
            }
        },1000);
    }

    public void onLoadNotification(String data) {
        mtvNotification.setText(data);
    }

    /**
     * 实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
     * exp:KeyEvent.ACTION_UP
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:    //返回键
                try {
                    exitBy2Click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 双击退出函数
     */
    private long exitTime = 0;

    private void exitBy2Click() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            AppUtils.getApplication().exit();
        }
    }
}
