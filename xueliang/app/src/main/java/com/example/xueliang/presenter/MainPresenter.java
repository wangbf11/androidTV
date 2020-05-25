package com.example.xueliang.presenter;


import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.xueliang.activity.LoginActivity;
import com.example.xueliang.activity.MainActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.bean.AppLogoInfoBean;
import com.example.xueliang.bean.UserInfoEntity;
import com.example.xueliang.network.ResponceSubscriber;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.SPUtil;
import com.example.xueliang.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPresenter extends BasePresenter<MainActivity> {

    private int mTime;

    public MainPresenter(MainActivity view) {
        this.view = view;
    }

    public void processLogic() {
        AppLogoInfoBean appLogoInfo = SPUtil.getAppLogoInfo();
        String time = appLogoInfo.getTime();
        mTime = 10 *60* 1000 ; //默认10分钟
        try {
            int i = Integer.parseInt(time);
            mTime = i *60* 1000 ; //默认10分钟
        }catch (Exception e){
            e.printStackTrace();
        }

        getMainNotice();
    }

    /**
     * 获取公告
     */
    private void getMainNotice() {
        RetrofitManager.getDefault().getNotice()
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List>() {
                    @Override
                    protected void onSucess(List list) {
                        String notice = "";
                        if (null != list && list.size() >0){
                            Map map = (Map)list.get(0);
                            notice = (String)map.get("notice");
                        }
                        if (null != view){
                            view.onLoadNotice(notice);
                        }
                        getMainNotification();
                        getLogInfo();
                    }

                    @Override
                    protected void onFail(String err) {
                        if (null != view){
                            view.onLoadNotice("");
                        }
                        Log.e("err","err");
                        getMainNotification();
                        getLogInfo();
                    }
                });
    }


    /**
     * 获取通知
     */
    private void getMainNotification() {
        RetrofitManager.getDefault().getNotification()
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List>() {
                    @Override
                    protected void onSucess(List list) {
                        String notication = "";
                        if (null != list && list.size() >0){
                            Map map = (Map)list.get(0);
                            notication = (String)map.get("notication");
                        }
                        if (null != view && notication != null){
                            view.onLoadNotification(notication);
                        }
                        new Handler().postDelayed(progressRunnable, mTime);
                    }

                    @Override
                    protected void onFail(String err) {
                        if (null != view ){
                            view.onLoadNotification("");
                        }
                        new Handler().postDelayed(progressRunnable, mTime);
                    }
                });
    }


    /**
     * 获取登录状态
     */
    public void getLogInfo() {
        Map<String, Object> params = new HashMap<>();
        String imei =  AppUtils.getIMEI();
        params.put("uuid", imei);
        RetrofitManager.getDefault().getLoginInfo(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<UserInfoEntity>() {
                    @Override
                    protected void onSucess(UserInfoEntity userInfoEntity) {
                        if (view != null && userInfoEntity != null) {
                            if (userInfoEntity.getMsgState() == 2){
                                AppUtils.getApplication().exit();
                                SPUtil.removeToken();
                                SPUtil.removeUserInfo();
                                Intent intent = new Intent(view, LoginActivity.class);
                                view.startActivity(intent);
                            }else {
                                if (StringUtils.isNotBlank(userInfoEntity.getToken())) {
                                    SPUtil.keepUserInfo(userInfoEntity);
                                    SPUtil.keepToken(userInfoEntity.getToken());
                                    if (null != view){
                                        view.onLoadUserInfo(userInfoEntity);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    protected void onFail(String err) {

                    }
                });
    }

    public Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            getMainNotice();
        }
    };

}
