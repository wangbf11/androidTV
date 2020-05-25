package com.example.xueliang.network;


import android.content.Context;
import android.content.Intent;

import com.example.xueliang.activity.LoginActivity;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.SPUtil;
import com.example.xueliang.utils.StringUtils;
import com.google.gson.JsonSyntaxException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * 通用订阅者,用于统一处理回调
 */
public abstract class ResponceSubscriber2<T> implements Observer<T> {

    private Context mContext;
    private Disposable mDisposable;

    protected boolean showDialog() {
        return true;
    }

    /**
     * @param context  context
     * @param mMessage dialog message
     */
    public ResponceSubscriber2(Context context, String ttile, String mMessage) {
        this.mContext = context;
    }


    public ResponceSubscriber2(Context context, String mMessage) {
        this(context, null, mMessage);

    }

    /**
     * @param context context 加入dialog
     */
    public ResponceSubscriber2(Context context) {
        this(context, "请稍后……");
    }

    public ResponceSubscriber2() {
        mContext = AppUtils.getApplication();
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(Disposable b) {
        mDisposable = b;
    }

    @Override
    public void onNext(T mBaseModel) {
        if (null != mDisposable) {
            mDisposable.dispose();
        }
        onSucess(mBaseModel);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (null != mDisposable) {
            mDisposable.dispose();
        }
        if (!RFNetUtil.isNetworkConnected()) { //判断网络
            onFail("网络不可用");
        } else if (e instanceof HttpException) {
            onFail(e.getMessage());
        } else if (e instanceof HttpCodeException) {
            onFail(e.getMessage());
            if (((HttpCodeException) e).code() == 203){
                if (StringUtils.isNotBlank(SPUtil.getToken())){
                    //如果是登录状态就 退出登录
                    AppUtils.getApplication().exit();
                    SPUtil.removeToken();
                    SPUtil.removeUserInfo();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        }else if (e instanceof ServerException) {
            onFail(e.getMessage());
        }else if (e instanceof JsonSyntaxException) {
            onFail("数据解析异常");
        } else if (e instanceof NullPointerException) {
            onFail(e.getMessage());
        } else {
            //其余不知名错误
            onFail("请求失败，请稍后再试...");
        }

    }

    protected abstract void onSucess(T t);

    //默认实现
    protected void onFail(String message) {
//        ToastUtils.show(message);
    }


}
