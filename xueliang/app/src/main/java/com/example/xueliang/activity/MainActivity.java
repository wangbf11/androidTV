package com.example.xueliang.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.bean.AppLogoInfoBean;
import com.example.xueliang.bean.UserInfoEntity;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.presenter.MainPresenter;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.DialogUtil;
import com.example.xueliang.utils.SPUtil;
import com.example.xueliang.utils.ScreenUtils;
import com.example.xueliang.utils.ToastUtils;
import com.example.xueliang.view.listener.MyFocusChange;
import com.example.xueliang.view.readview.PageLoader;
import com.example.xueliang.view.readview.PageView;
import com.example.xueliang.view.readview.TxtChapter;

import java.util.HashMap;
import java.util.Map;

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
    private View mll_notification;
    private boolean mIsDestroy;
    private TextView tv_user;
    private TextView tv_location;
    private ImageView main_logo1;
    private ImageView main_logo2;

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

        AppLogoInfoBean appLogoInfoBean =  SPUtil.getAppLogoInfo();
        if (appLogoInfoBean != null ){
            if (appLogoInfoBean.getLogo() != null) {
                Glide.with(this).load(appLogoInfoBean.getLogo()).into(main_logo1);
                Glide.with(this).load(appLogoInfoBean.getLogo()).into(main_logo2);
            }
        }
    }

    @Override
    public void initView() {
        tv_user = findViewById(R.id.tv_user);
        tv_location = findViewById(R.id.tv_location);
        mPvPage = findViewById(R.id.read_pv_page);
        mLogout = findViewById(R.id.tv_logout);
        ll_monitor = findViewById(R.id.ll_monitor);
        mtvNotification = findViewById(R.id.mtv_notification);
        mll_notification = findViewById(R.id.ll_notification);
        dot_ll = (LinearLayout) findViewById(R.id.dot_ll);
        main_logo1 = findViewById(R.id.main_logo1);
        main_logo2 = findViewById(R.id.main_logo2);

        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        UserInfoEntity userInfo = SPUtil.getUserInfo();
        String nickName = userInfo.getNickName();
        String phone = userInfo.getPhone();
        tv_user.setText("负责人："+nickName +" " + phone);

        mPageLoader = mPvPage.getPageLoader();
        ll_monitor.setOnFocusChangeListener(new MyFocusChange());

        ll_monitor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(mContext.getResources().getDrawable(R.drawable.bg_boder2w));
                } else {
                    v.setBackground(null);
                }
            }

        });


        mll_notification.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(mContext.getResources().getDrawable(R.drawable.bg_boder2w));
                } else {
                    v.setBackground(null);
                }
            }

        });
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
                        String mUuid = AppUtils.getIMEI();
                        Map<String, Object> params = new HashMap<>();
                        params.put("uuid", mUuid);
                        RetrofitManager.getDefault().loginOut(params)
                                .compose(RxSchedulerUtils::toSimpleSingle)
                                .subscribe(new ResponceSubscriber2<UserInfoEntity>() {
                                    @Override
                                    protected void onSucess(UserInfoEntity list) {
                                        if (null != list){
                                            AppUtils.getApplication().exit();
                                            SPUtil.removeToken();
                                            SPUtil.removeUserInfo();
                                            Intent intent = new Intent(mContext, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    protected void onFail(String err) {
                                        ToastUtils.show("退出失败");
                                    }
                                });
                    }, "取 消", (dialog, which) -> {
                        dialog.dismiss();
                    }, false);
        });



        ll_monitor.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(this, MonitorListActivity.class);
            startActivity(intent);
        });
        mll_notification.setOnClickListener(v->{
            DialogUtil.showAlertNotice(mContext, "内容详情", mtvNotification.getText().toString(),
                     (dialog, which) -> {
                        AppUtils.getApplication().exit();
                        SPUtil.removeToken();
                        SPUtil.removeUserInfo();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }, "关 闭", (dialog, which) -> {
                        dialog.dismiss();
                    }, false);
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
            if (mIsDestroy){
                return;
            }
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
        Spanned spanned = Html.fromHtml(data);
        TxtChapter txtChapter = new TxtChapter();
        txtChapter.setBody(spanned.toString());
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
        Spanned spanned = Html.fromHtml(data);
        mtvNotification.setText(spanned.toString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroy = true;
    }
}
