package com.example.xueliang.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.xueliang.base.BaseApplication;
import com.example.xueliang.bean.AppLogoInfoBean;
import com.example.xueliang.bean.UserInfoEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * SharedPreferences 工具类
 * String key->MD5 | value->aes
 * 其他 key->MD5
 *
 * @author weiwei
 */
public class SPUtil {


    public static String PREFERENCE_NAME = "xueliang";

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        try {
            editor.putString(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editor.commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String encryptValue = settings.getString(key, defaultValue);
        if (encryptValue == null) {
            return null;
        }
        if (encryptValue.equals(defaultValue)) {
            return defaultValue;
        } else {
            try {
                return encryptValue;
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }


    public static String getToken() {
        return getString(BaseApplication.getContext(),"pipe_token","");
    }

    public static void keepToken(String token) {
        putString(BaseApplication.getContext(),"pipe_token",token);
    }

    public static void removeToken(){
        keepToken("");
    }

    public static void keepUserInfo(UserInfoEntity userInfo) {
        Gson gson = new Gson();
        putString(BaseApplication.getContext(),"pipe_user_info",gson.toJson(userInfo));
    }

    public static UserInfoEntity getUserInfo() {
        String read = getString(BaseApplication.getContext(),"pipe_user_info","");
        if (StringUtils.isBlank(read)) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(read, (new TypeToken<UserInfoEntity>() {}).getType());
        }
    }

    public static void keepAppLogoInfo(AppLogoInfoBean appLogoInfo) {
        Gson gson = new Gson();
        putString(BaseApplication.getContext(),"pipe_user_info",gson.toJson(appLogoInfo));
    }

    public static AppLogoInfoBean getAppLogoInfo() {
        String read = getString(BaseApplication.getContext(),"pipe_user_info","");
        if (StringUtils.isBlank(read)) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(read, (new TypeToken<AppLogoInfoBean>() {}).getType());
        }
    }

    public static void removeUserInfo(){
        putString(BaseApplication.getContext(),"pipe_user_info","");
    }

    public static boolean clear() {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.edit().clear().commit();
    }
}
