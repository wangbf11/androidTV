package com.example.xueliang.network.errorcode;

/**
 * Created by wbf on 2019/1/15.
 */

public class HttpCodeResult {
    private int httpCode; //http状态码
    private String error; //错误信息
    private String code; //错误信息

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
