package com.example.xueliang.utils;

import com.example.xueliang.bean.PointBean;

public class PointUtils {

    /**
     * 根据bean获取url
     * @param pointBean
     * @return
     */
    public static String getPlayerUrl(PointBean pointBean) {
        String url = pointBean.getRtspSrc();
        if (StringUtils.isEmpty(url) && StringUtils.isNotEmpty(pointBean.getRtmpSrc())) {
            url = pointBean.getRtmpSrc();
        }
        if (StringUtils.isEmpty(url)) {
            url = ""; //测试代码
        }
        return url;
    }
}
