package com.example.xueliang.network;

import android.os.Handler;
import android.util.Log;

import com.example.xueliang.config.AppConfig;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.JSONUtil;
import com.example.xueliang.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by wbf on 2019/1/8.
 */

public class RetrofitManager {

    public static final String BASE_URL = AppConfig.baseUrl;
    private Handler handler=null;
    //短缓存有效期为1分钟
    public static final int CACHE_STALE_SHORT = 60;
    //长缓存有效期为7天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;

    public static final String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=UTF-8";
    public static final String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    public static final String CACHE_CONTROL_NETWORK = "max-age=0";
    private static OkHttpClient mOkHttpClient;
    public  final XueLiangService service;
    private static RetrofitManager mRetrofitManager;
    /**
     * 创建单列
     */
    public static RetrofitManager builder() {
        if (mRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager();
                }
                return mRetrofitManager;
            }
        }else {
            return mRetrofitManager;
        }
    }

    public static XueLiangService getDefault() {
        return builder().service;
    }

    protected RetrofitManager() {
        initOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(XueLiangService.class);
    }

    private void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(AppUtils.getApplication().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .retryOnConnectionFailure(true)
                            .sslSocketFactory(SSLFactoryManager.getDefaultTrust())
                            .hostnameVerifier(SSLFactoryManager.getDefaultHostnameVerifierTrust())
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            String token = "";

            if (StringUtils.isBlank(token)){
//                token = WeTalkCacheUtil.getToken();
            }
            if (!RFNetUtil.isNetworkConnected()) {
                request = request.newBuilder().addHeader("Authorization","")
                        .addHeader("version", AppUtils.getAppVersion())
                        .addHeader("system", String.valueOf(2))
                        .cacheControl(CacheControl.FORCE_CACHE).build();
            }else {
                request = request.newBuilder().addHeader("Authorization","Bearer " + token)
                        .addHeader("version", AppUtils.getAppVersion())
                        .addHeader("system", String.valueOf(2))
                        .build();
            }

            Log.d("request ----------> ", request.url().toString());
            Response originalResponse = chain.proceed(request);
            if (originalResponse.code() != 200){
                //httpcode 不是200的都修改成200 把code放进status 里面
                MediaType contentType = originalResponse.body().contentType();
                String string = originalResponse.body().string();
                Map map = JSONUtil.parseJSON(string, Map.class);
                map.put("httpCode",originalResponse.code());
                ResponseBody body = ResponseBody.create(contentType,JSONUtil.toJSON(map));
                return  originalResponse.newBuilder().code(200).body(body).build();
            }
            Log.d("response ----------> " , originalResponse + "");
            if (RFNetUtil.isNetworkConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse
                        .newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse
                        .newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma").build();
            }
        }
    };
}



