package com.example.xueliang.utils;

import com.example.xueliang.bean.PointBean;

public class PointUtils {

    /**
     * 根据bean获取url
     * @param pointBean
     * @return
     */
    public static String getPlayerUrl(PointBean pointBean) {
        String url = pointBean.getPlayrealUrl();
//        if (StringUtils.isEmpty(url) && StringUtils.isNotEmpty(pointBean.getRtmpSrc())) {
//            url = pointBean.getRtmpSrc();
//        }
//        if (StringUtils.isEmpty(url)) {
//            url = "rtmp://58.200.131.2:1935/livetv/hunantv"; //测试代码
//        }
        return url;
    }
}
