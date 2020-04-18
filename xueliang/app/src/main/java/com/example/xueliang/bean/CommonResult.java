package com.example.xueliang.bean;

/**
 * Created by wbf on 2019/1/15.
 */

public class CommonResult<T> {
    private T list;
    private String msg;

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
