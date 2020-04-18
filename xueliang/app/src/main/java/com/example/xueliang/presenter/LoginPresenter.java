package com.example.xueliang.presenter;


import android.util.Log;

import com.example.xueliang.activity.LoginActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.network.ResponceSubscriber;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.JSONUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPresenter extends BasePresenter<LoginActivity> {
    public LoginPresenter(LoginActivity view) {
        this.view = view;
    }

    /**
     * 获取通知
     */
    public void getLoginQrCode() {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", AppUtils.getIMEI());
        RetrofitManager.getDefault().getLoginQrCode()
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List>() {
                    @Override
                    protected void onSucess(List list) {
                        if (view != null){
                            view.onGetQrCode(JSONUtil.toJSON(list));
                        }
                    }

                    @Override
                    protected void onFail(String err) {

                        Log.e("err","err");
                    }
                });
    }




}
