package com.example.xueliang.presenter;


import com.example.xueliang.activity.MainActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;

import java.util.HashMap;
import java.util.Map;

public class MainPresenter extends BasePresenter<MainActivity> {
    public MainPresenter(MainActivity view) {
        this.view = view;
    }

    public void processLogic() {
        Map<String, Object> params = new HashMap<>();
        params.put("phoneContactList", "");
        RetrofitManager.getDefault().getdemo(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<Map<String,Object>>() {
                    @Override
                    protected void onSucess(Map<String,Object> myMessage) {
                    }

                    @Override
                    protected void onFail(String err) {
                    }
                });
    }


}
