package com.example.xueliang.config;


/**
 * Created by wbf on on 2018/6/15.
 */
public class Constant {

    public static final int REQUEST_CAPTURE = 998;  //相机
    public static final int IMAGE_PICKER = 999;  //相册
    public static final int REQUEST_CODE_CROUP_PHOTO = 997;  //裁剪

    /**
     * 搜索记录
     */
    public static final String FLAG_RECORD = "Android.Record";
    public static final String RECORD = "Record";

    /**
     * 登陆类型
     */
    public static final String FLAG_TYPE = "Android.Login.type";
    public static final String TYPE = "Login.type";

    /**
     * 语言类型 缓存使用的key
     */
    public static final String LANGUAGE = "language";  //分别是 ID  en_US  zh_CN

    /**
     * 字体大小等级 缓存使用的key
     */
    public static final String FONT_SIZE_GRADE = "font_size_grade";


    //app启动来源  0：桌面启动 1、通知启动 2、分享启动 3、浏览器启动
    public static final int APPFROM_DESKTOP=0;
    public static final int APPFROM_NOTIFICATION=1;
    public static final int APPFROM_SYSTEMSHARE=2;
    public static final int APPFROM_URL=3;

    public static final String AUDIO_TYPE = "AUDIO";//type 1
    public static final String VIDEO_TYPE = "VIDEO";//type 2

}

