package com.example.xueliang.presenter;


import android.util.Log;

import com.example.xueliang.activity.MonitorActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;

import java.util.HashMap;
import java.util.Map;

public class MonitorPresenter extends BasePresenter<MonitorActivity> {
    public MonitorPresenter(MonitorActivity view) {
        this.view = view;
    }

    public void processLogic() {
        Map<String, Object> params = new HashMap<>();
        params.put("phoneContactList", "");
        RetrofitManager.getDefault().getList()
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<Map<String,Object>>() {
                    @Override
                    protected void onSucess(Map<String,Object> obj) {
                        Log.e("dd","dd");
                    }

                    @Override
                    protected void onFail(String err) {

                        Log.e("err","err");
                    }
                });
    }


}
