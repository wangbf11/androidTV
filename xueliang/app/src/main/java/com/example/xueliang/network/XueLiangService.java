package com.example.xueliang.network;


import com.example.xueliang.bean.CommonResult;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by wbf on 2020/3/21
 */

public interface XueLiangService {
    /**
     * @return
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @POST("/api/post/user-info")
    Observable<Map<String,Object>> getdemo(@Body Map<String, Object> params);

    /**
     * @return
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @GET("/interface/rest/http/xlgc/wb-test.htm")
    Observable<Map<String,Object>> getList();


    @Multipart
    @POST("{domain}")
    Observable<CommonResult<Map<String,Object>>> upLoadImg(@Path("domain") String domain, @PartMap Map<String, RequestBody> params);

    /**
     * 获取通知
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @POST("/interface/rest/http/xlwb/xlgc-wb-jdh-sytz.htm")
    Observable<CommonResult<List>> getLoginQrCode();

    /**
     * 获取公告
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @POST("/interface/rest/http/xlwb/xlgc-wb-jdh-sygg.htm")
    Observable<CommonResult<List>> getNotice();
    /**
     * 获取通知
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @POST("/interface/rest/http/xlwb/xlgc-wb-jdh-sytz.htm")
    Observable<CommonResult<List>> getNotification();
}
