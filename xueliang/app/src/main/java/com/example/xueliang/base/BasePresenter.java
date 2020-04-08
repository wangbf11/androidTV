package com.example.xueliang.base;



import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wbf on 2019/1/9.
 */

public abstract class BasePresenter<T extends LoadCallBack> {

    protected T view;

    public Boolean checkBool(String s) {
        try {
            if (s.equals("1")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected CompositeDisposable mDisposable;

    protected void addDisposable(Disposable subscription) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(subscription);
    }

    protected void unSubscribe() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public void onDestroy() {
        view = null;
        unSubscribe();
    }
}
