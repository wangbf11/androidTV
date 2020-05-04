package com.example.xueliang.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wbf on 2019/1/16.
 */

public class ParamsUtil {

    public static Map<String, Object> getDefaultParams() {
        Map<String, Object> params = new HashMap<>();
        return params;
    }

    /**
     * map转url params
     */
    public static String map2urlParams(Map<String, Object> params) {
        if (null == params){
            return "";
        }
        StringBuffer urlParams = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            urlParams.append(entry.getKey());
            urlParams.append("=");
            urlParams.append(entry.getValue());
            urlParams.append("&");
        }
        String s = urlParams.toString();
        String substring = "";
        if (s.length() > 0){
            substring = s.substring(0, s.length() - 1);
        }
        return substring;
    }



    /**
     * 获取url的参数
     * @param url
     * @return
     */
    public static HashMap<String, String> getUrlParams(String url){
        try {
            URL url1 = new URL(url);
            if(null != url1) {
                String query = url1.getQuery();
                if(StringUtils.isNotEmpty(query)) {
                    String[] argAry = {query};
                    if (query.contains("&")){
                        argAry = query.split("&");
                    }
                    HashMap<String, String> argMap= new HashMap<String, String>();
                    for (String arg : argAry){
                        String[] argAry1 = arg.split("=");
                        if (argAry1.length == 2){
                            argMap.put(argAry1[0],argAry1[1]);
                        }else {
                            continue;
                        }
                    }
                    return argMap;
                }else {
                    return null;
                }
            }else {
                return null;
            }
        } catch (MalformedURLException e) {
            //如果不是url 但里面有url
            if (url.contains("?")){
                int index = url.indexOf("?");
                String argStr = url.substring(index + 1);
                String[] argAry = {argStr};
                if (argStr.contains("&")){
                    argAry = argStr.split("&");
                }
                HashMap<String, String> argMap= new HashMap<String, String>();
                for (String arg : argAry){
                    String[] argAry1 = arg.split("=");
                    if (argAry1.length == 2){
                        argMap.put(argAry1[0],argAry1[1]);
                    }else {
                        continue;
                    }
                }
                return argMap;
            }else {
                return null;
            }
        }
    }
    /**
     * 移除url的参数
     * @param url
     * @return
     */
    public static String removeUrlParam(String url, String name){
        HashMap<String, String> urlParams = getUrlParams(url);
        if (null != urlParams && urlParams.containsKey(name)){
            String param = "&" + name + "=" + urlParams.get(name);
            url = url.replace(param,"");
            String param2 = "?" + name + "=" + urlParams.get(name);
            url = url.replace(param2,"");
        }
        return url;
    }
}
