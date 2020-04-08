package com.example.xueliang.base;

public interface LoadCallBack<T> {
    public void onLoad(T data);

    public void onLoadFail(String message);
}
