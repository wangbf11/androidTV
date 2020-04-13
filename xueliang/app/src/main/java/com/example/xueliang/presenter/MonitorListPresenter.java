package com.example.xueliang.presenter;


import com.example.xueliang.activity.MonitorListActivity;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.utils.JSONUtil;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MonitorListPresenter extends BasePresenter<MonitorListActivity> {
    public MonitorListPresenter(MonitorListActivity view) {
        this.view = view;
    }

    public void processLogic() {
        Disposable disposable = Single.create(new SingleOnSubscribe<List<TownBean>>() {
            @Override
            public void subscribe(SingleEmitter<List<TownBean>> singleSubscriber) throws Exception {
                //子线程处理数据 避免ui阻塞
                String countryListStr = getStringFromAssets("data.json");
                List<TownBean> list = JSONUtil.parseJSON(countryListStr, (new TypeToken<List<TownBean>>() {
                }).getType());
                singleSubscriber.onSuccess(list); //发射数据
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TownBean>>() {
                    @Override
                    public void accept(List<TownBean> list) throws Exception {
                        //主线程 设置数据更新ui
                        if (null != view){
                            view.onLoad(list);
                        }
                    }
                });

        addDisposable(disposable);
    }

    public String getStringFromAssets(String file) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream listFile =  view.getAssets().open(new File(file).getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(listFile));

            String line = null;
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }



}
