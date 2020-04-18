package com.example.xueliang.presenter;


import android.util.Log;

import com.example.xueliang.activity.MainActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.network.ResponceSubscriber;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;

import java.util.List;
import java.util.Map;

public class MainPresenter extends BasePresenter<MainActivity> {
    public MainPresenter(MainActivity view) {
        this.view = view;
    }

    public void processLogic() {
        getMainNotice();
        getMainNotification();
    }

    /**
     * 获取通知
     */
    private void getMainNotice() {
        RetrofitManager.getDefault().getNotice()
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List>() {
                    @Override
                    protected void onSucess(List list) {
                        if (null != list && list.size() >0){
                            Map map = (Map)list.get(0);
                            String notice = (String)map.get("notice");
                            if (null != view){
                                view.onLoadNotice(notice);
                            }
                        }
                    }

                    @Override
                    protected void onFail(String err) {

                        Log.e("err","err");
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
                        if (null != list && list.size() >0){
                            Map map = (Map)list.get(0);
                            String notication = (String)map.get("notication");
                            if (null != view){
                                view.onLoadNotification(notication);
                            }
                        }
                    }

                    @Override
                    protected void onFail(String err) {

                        Log.e("err","err");
                    }
                });
    }

}
