package com.example.xueliang.presenter;


import android.os.Handler;
import android.util.Log;

import com.example.xueliang.activity.LoginActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.bean.AppUpdateInfoBean;
import com.example.xueliang.bean.QrCodeBean;
import com.example.xueliang.bean.UserInfoEntity;
import com.example.xueliang.network.ResponceSubscriber;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.ParamsUtil;
import com.example.xueliang.utils.SPUtil;
import com.example.xueliang.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPresenter extends BasePresenter<LoginActivity> {

    private int mTimer = 3000;
    private String mUuid;

    public LoginPresenter(LoginActivity view) {
        this.view = view;
    }

    /**
     * 获取登录注册二维码
     */
    public void getLoginQrCode() {
        Map<String, Object> params = new HashMap<>();
        mUuid = AppUtils.getIMEI();
        params.put("uuid", mUuid);
        RetrofitManager.getDefault().getLoginQrCode(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List<QrCodeBean>>() {
                    @Override
                    protected void onSucess(List<QrCodeBean> list) {
                        if (view != null && list != null && list.size() > 0) {
                            QrCodeBean qrCodeBean = list.get(0);
                            String loginUrl = qrCodeBean.getLoginUrl();
                            int timer = qrCodeBean.getTimer();
                            mTimer = timer * 1000;
                            if (mTimer <= 0) {
                                mTimer = 3000;
                            }
                            Map<String, Object> urlparams = new HashMap<>();
                            urlparams.put("uuid", mUuid);
                            String map2urlParams = ParamsUtil.map2urlParams(urlparams);
                            loginUrl = loginUrl + "?" + map2urlParams;
                            view.onGetQrCode(loginUrl);
                            getLogInfo();
                        } else {
                            Log.e("err", "err");
                        }
                    }

                    @Override
                    protected void onFail(String err) {
                        Log.e("err", "err");
                    }
                });
    }


    /**
     * 获取登录状态
     */
    public void getLogInfo() {
        Map<String, Object> params = new HashMap<>();
        String imei = mUuid;
        if (StringUtils.isBlank(imei)) {
            imei = AppUtils.getIMEI();
        }
        params.put("uuid", imei);
        RetrofitManager.getDefault().getLoginInfo(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<UserInfoEntity>() {
                    @Override
                    protected void onSucess(UserInfoEntity userInfoEntity) {
                        if (view != null && userInfoEntity != null) {
                            if (StringUtils.isNotBlank(userInfoEntity.getToken())) {
                                SPUtil.keepUserInfo(userInfoEntity);
                                SPUtil.keepToken(userInfoEntity.getToken());
                                view.onLoginSuccess();
                            } else {
                                new Handler().postDelayed(progressRunnable, mTimer);
                            }
                        } else {
                            if (view != null){
                                new Handler().postDelayed(progressRunnable, mTimer);
                            }
                        }
                    }

                    @Override
                    protected void onFail(String err) {
                        new Handler().postDelayed(progressRunnable, mTimer);
                    }
                });
    }


    public Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            getLogInfo();
            Log.e("getLogInfo", "循环获取登录状态");
        }
    };


    public void getApkIsUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("version", AppUtils.getAppVersion());
        RetrofitManager.getDefault().getApkisUpdate(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List<AppUpdateInfoBean>>() {
                    @Override
                    protected void onSucess(List<AppUpdateInfoBean> list) {
                        Log.e("getApkisUpdate", "list="+list);
                        if (view != null && list != null && list.size() > 0) {
                            AppUpdateInfoBean appUpdateInfoBean = list.get(0);
                            if ("0".equals(appUpdateInfoBean.getType())){//无需更新

                            }else if ("1".equals(appUpdateInfoBean.getType())){//需要更新

                            }else if ("2".equals(appUpdateInfoBean.getType())){//强制更新

                            }
                        }
                    }

                    @Override
                    protected void onFail(String err) {
                        Log.e("err", "err");
                    }
                });
    }

}
