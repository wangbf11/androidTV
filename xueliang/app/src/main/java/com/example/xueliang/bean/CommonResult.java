package com.example.xueliang.bean;

/**
 * Created by wbf on 2019/1/15.
 */

public class CommonResult<T> {
    private String code; //"OK" 是正常请求

    private T data;
    private Object content;//没用到
    private String msg; //没用到
    private String status; //状态信息
    private String timestamp; //时间

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
