/**
 * 登录注册错误码提示
 */
package com.example.xueliang.network.errorcode;

public enum ErrorDeCode {
    e3000413("#3000413", "60秒内不可重复发送短信"),
    e3000412("#3000412", "用户名或密码不正确"),
    e8000008("#8000008", "短信验证码发送过去频繁，请稍后再试"),
    e8000009("#8000009", "短信验证码服务临时关闭，请联系管理员"),
    e8000007("#8000007", "短信验证码发送失败，请稍后再试");

    public String code;
    private String content;
    private String desc;

    private ErrorDeCode(String code, String content, String desc) {
        this.code = code;
        this.desc = desc;
        this.content = content;
    }

    private ErrorDeCode(String code, String content) {
        this.code = code;
        this.content = content;
    }

    public String code() {
        return this.code;
    }

    public String content() {
        return this.content;
    }

    public String desc() {
        return this.desc == null ? "网络异常，请稍候再试" : this.desc;
    }

    public static String desc(String code) {
        ErrorDeCode[] var4;
        int var3 = (var4 = values()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            ErrorDeCode error = var4[var2];
            if (error.code == code) {
                return error.desc();
            }
        }

        return "未知错误";
    }
}
