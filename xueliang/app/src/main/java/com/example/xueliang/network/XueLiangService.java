package com.example.xueliang.network;


import com.example.xueliang.bean.CommonResult;
import com.example.xueliang.bean.CommonResult2;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.QrCodeBean;
import com.example.xueliang.bean.TownBean;
import com.example.xueliang.bean.UserInfoEntity;

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
import retrofit2.http.QueryMap;

/**
 * Created by wbf on 2020/3/21
 */

public interface XueLiangService {
    /**
     * @return
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @POST("/api/post/user-info")
    Observable<Map<String, Object>> getdemo(@Body Map<String, Object> params);

    /**
     * @return
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @GET("/interface/rest/http/xlgc/wb-test.htm")
    Observable<Map<String, Object>> getList();


    @Multipart
    @POST("{domain}")
    Observable<CommonResult<Map<String, Object>>> upLoadImg(@Path("domain") String domain, @PartMap Map<String, RequestBody> params);

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

    /**
     * 获取登录和注册 二维码
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @POST("/interface/rest/http/xlwb/xlgc-wb-jdh-hqdlhzcdzjk.htm")
    Observable<CommonResult<List<QrCodeBean>>> getLoginQrCode(@Body Map<String, Object> params);

    /**
     * 获取登录和注册状态
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @POST("/interface/rest/http/xlwb/xlgc-wb-jdh-hqdlhzczt.htm")
    Observable<CommonResult<List<UserInfoEntity>>> getLoginInfo(@Body Map<String, Object> params);

    /**
     * 获取镇和村数据
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @GET("/interface/rest/http/xlwb/xlgc-wb-jdh-xzcdw.htm")
    Observable<CommonResult2<List<TownBean>>> getTownAndCunList(@QueryMap Map<String, Object> params);

    /**
     * 根据村id获取点数据
     */
    @Headers({"Content-Type:application/json; charset=utf-8", RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_CONTROL_NETWORK})
    @GET("/interface/rest/http/xlgc/xltzxt-ywgl-jkspgl-ssjkspgl-ssjkspxx.htm")
    Observable<CommonResult<List<PointBean>>> getPointListByCunId(@QueryMap Map<String, Object> params);
}
