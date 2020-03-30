package com.example.xueliang.network;

import com.example.xueliang.network.errorcode.ErrorCode;
import com.example.xueliang.network.errorcode.HttpCodeResult;
import com.example.xueliang.utils.JSONUtil;
import com.example.xueliang.utils.StringUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 当返回失败信息的时候，很有可能 信息T 的数据结构和成功状态不一样 导致 直接走了rxjava  的onError回调，得不到失败的信息(就有些傻吊后台会这么玩)
 * Created by tamgming on 2017/10/23.
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {


    private final Gson gson;
    private final Type type;


    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();

        HttpCodeResult codeResult = JSONUtil.parseJSON(response, HttpCodeResult.class);
        String error = "";
        if (null != codeResult){
            error = codeResult.getError();
            if (codeResult.getHttpCode() == 203){
                throw new HttpCodeException(error,203);
            } else if (codeResult.getHttpCode() == 400 && ErrorCode.ERROR_CODE_1000404.equals(codeResult.getCode())) {
                return gson.fromJson(response, type); // 未注册#1000404的情况情况下，跳转成功。会调注册接口
            }else if (codeResult.getHttpCode() == 400 && ErrorCode.ERROR_CODE_3000413.equals(codeResult.getCode())) {
                return gson.fromJson(response, type); // 短信不能重复发送60秒内
            }
        }else {
            //httpCode为200 而且 body为空 就会走这里
            if (StringUtils.isBlank(response)){
                return gson.fromJson("{\"code\":\"OK\"}", type);
            }
        }

        if (StringUtils.isNotBlank(error)){
            throw new ServerException(error);
        }else {
            return gson.fromJson(response, type);
        }
    }

}


