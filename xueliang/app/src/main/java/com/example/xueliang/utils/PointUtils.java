package com.example.xueliang.utils;

import com.example.xueliang.bean.PointBean;

public class PointUtils {

    /**
     * 根据bean获取url
     * @param pointBean
     * @return
     */
    public static String getPlayerUrl(PointBean pointBean) {
        String url = pointBean.getRtmpSrc();
        if (url == null && pointBean.getRtspSrc() != null) {
            url = pointBean.getRtspSrc();
        }
        if (url == null) {
            url = "rtmp://58.200.131.2:1935/livetv/hunantv"; //测试代码
        }
        return url;
    }
}
