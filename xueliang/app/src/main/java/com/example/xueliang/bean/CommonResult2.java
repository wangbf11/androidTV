package com.example.xueliang.bean;

/**
 * Created by wbf on 2019/1/15.
 */

public class CommonResult2<T> {
    private T result;
    private String msg;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
