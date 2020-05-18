package com.example.xueliang.presenter;


import com.example.xueliang.activity.MonitorActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.bean.CommonResult2;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.network.ResponceSubscriber2;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.utils.StringUtils;
import com.example.xueliang.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorPresenter extends BasePresenter<MonitorActivity> {
    public MonitorPresenter(MonitorActivity view) {
        this.view = view;
    }

    public void processLogic() {
        Map<String, Object> params = new HashMap<>();
        RetrofitManager.getDefault().getTownAndCunList(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<CommonResult2<List<TownBean>>>() {
                    @Override
                    protected void onSucess(CommonResult2<List<TownBean>> data) {
                        if (null == data){
                            return;
                        }
                        List<TownBean> list = data.getResult();
                        if (view != null &&list != null &&list.size() >0) {
                            view.onLoad(list);
                        }else {
                            if (view != null){
                                view.onLoadFail("");
                            }
                        }
                    }

                    @Override
                    protected void onFail(String err) {
                        if (view != null){
                            view.onLoadFail("");
                        }
                    }
                });
    }

    /**
     * 获取登录注册二维码
     */
    public void oneKeyQZ(String pointId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", pointId);
        RetrofitManager.getDefault().oneKeyQZ(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber2<Map>() {
                    @Override
                    protected void onSucess(Map data) {
                        String msg = (String) data.get("msg");
                        if (data != null && StringUtils.isNotEmpty(msg)) {
                            Double msgState = (Double) data.get("msgState");
                            if (msgState == 1) {
                                ToastUtils.show("上传成功!");
                                if (view != null){
                                    view.onQZSucess("");
                                }
                            }else{
                                ToastUtils.show(msg);
                            }
                        }else{
                            ToastUtils.show("上报失败!");
                        }
                    }

                    @Override
                    protected void onFail(String err) {
                        ToastUtils.show("上报失败!");
                    }
                });
    }

}
