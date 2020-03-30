package com.example.xueliang.network;

/**
 * Created by wbf on 2019/1/18.
 */

public class HttpCodeException extends RuntimeException {
    private int code;
    public HttpCodeException(String mS,int code) {
        super(mS == null ? "" : mS);  // 防止异常信息返回为null 程序崩溃 （尝试在retrofit中全局设置 string 为 null的时候解析为“”，但失败）
        this.code = code;
    }

    public int code() {
        return code;
    }
}
