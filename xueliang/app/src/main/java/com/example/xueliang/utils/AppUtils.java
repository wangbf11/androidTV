package com.example.xueliang.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.xueliang.base.BaseApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    private static BaseApplication application;
    public static void init(BaseApplication application) {
        AppUtils.application = application;
    }

    public static BaseApplication getApplication() {
        return application;
    }

    /*
     * 获取手机屏幕尺寸，屏幕宽*屏幕高 如：480*800（单位px）
     *
     */
    public static  String getDisplayMetrics(){
        DisplayMetrics dm = new DisplayMetrics();
        dm = getApplication().getResources().getDisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "*" + dm.heightPixels;
    }
    /*
     * 获取手机屏幕宽  如：480px
     *
     */
    public static int getScreenWidth() {
        int screenWidth=0;
        try{
            DisplayMetrics dm = new DisplayMetrics();
            dm = getApplication().getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
        }catch (Exception e) {
        }
        return screenWidth;
    }
    /*
     * 获取手机屏幕高 如：800px
     *
     */
    public static int getScreenHeight() {
        int screenHeight=0;
        try{
            DisplayMetrics dm = new DisplayMetrics();
            dm = getApplication().getResources().getDisplayMetrics();
            screenHeight = dm.heightPixels;
        }catch (Exception e) {
        }
        return screenHeight;
    }

    /**
     * 获取Android设备唯一id
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI() {
        String imei;
        try {
//            TelephonyManager telephonyManager = (TelephonyManager)getApplication().getSystemService(Context.TELEPHONY_SERVICE);
//            imei = telephonyManager.getDeviceId();

            imei = Settings.Secure.getString(getApplication().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            imei = "";
        }
        return imei;
    }

    /**
     * 获取Android系统版本号
     */
    public static String getVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取Android系统对应的SDK版本号
     */
    public static int getVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前客户端版本编号
     */
    public static int getAppVersionCode() {
        try {
            PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return -1;
    }

    /**
     * 获取当前客户端版本编号
     */
    public static String getAppVersion() {
        try {
            PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    public static int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip( float pxValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static float getPixelsInCM(float cm, boolean isX) {
        return (cm / 2.54f) * (isX ? displayMetrics.xdpi : displayMetrics.ydpi);
    }

    private static String sDeviceAbi;
    private static String sRuntimeAbi;

    private static boolean is64bitDevice() {
        String abi = getDeviceAbi();
        return abi.equals("arm64-v8a") || abi.equals("x86_64");
    }

    private static String getDeviceAbi() {
        if (sDeviceAbi == null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sDeviceAbi = Build.SUPPORTED_ABIS[0].toLowerCase();
                } else {
                    sDeviceAbi = getsDeviceAbi();
                }
            } catch (NoSuchFieldError var5) {
                sDeviceAbi = getsDeviceAbi();
            }

        }

        return sDeviceAbi;
    }

    private static String getsDeviceAbi() {
        String sDeviceAbi;
        try {
            Process ex = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
            InputStreamReader ir = new InputStreamReader(ex.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            sDeviceAbi = input.readLine().toLowerCase();
            input.close();
            ir.close();
        } catch (IOException var4) {
            throw new RuntimeException("Can not detect device\'s ABI");
        }
        return sDeviceAbi;
    }

    public static String getRuntimeAbi() {
        if (sRuntimeAbi == null) {
            try {
                if (Build.VERSION.SDK_INT >= 21) {
                    throw new NoSuchFieldError();
                }

                String e = Build.CPU_ABI.toLowerCase();
                byte var2 = -1;
                switch (e.hashCode()) {
                    case -806050265:
                        if (e.equals("x86_64")) {
                            var2 = 4;
                        }
                        break;
                    case -738963905:
                        if (e.equals("armeabi")) {
                            var2 = 0;
                        }
                        break;
                    case 117110:
                        if (e.equals("x86")) {
                            var2 = 3;
                        }
                        break;
                    case 145444210:
                        if (e.equals("armeabi-v7a")) {
                            var2 = 1;
                        }
                        break;
                    case 1431565292:
                        if (e.equals("arm64-v8a")) {
                            var2 = 2;
                        }
                }

                switch (var2) {
                    case 0:
                    case 1:
                        sRuntimeAbi = "armeabi-v7a";
                        break;
                    case 2:
                        sRuntimeAbi = "arm64-v8a";
                        break;
                    case 3:
                        sRuntimeAbi = "x86";
                        break;
                    case 4:
                        sRuntimeAbi = "x86_64";
                        break;
                    default:
                        sRuntimeAbi = "unknown";
                        break;
                }
            } catch (NoSuchFieldError var4) {
                String arch = System.getProperty("os.arch").toLowerCase();
                byte var3 = -1;
                switch (arch.hashCode()) {
                    case -1409295825:
                        if (arch.equals("armv7l")) {
                            var3 = 6;
                        }
                        break;
                    case -1221096139:
                        if (arch.equals("aarch64")) {
                            var3 = 9;
                        }
                        break;
                    case -806050265:
                        if (arch.equals("x86_64")) {
                            var3 = 5;
                        }
                        break;
                    case -738963905:
                        if (arch.equals("armeabi")) {
                            var3 = 7;
                        }
                        break;
                    case 117046:
                        if (arch.equals("x64")) {
                            var3 = 4;
                        }
                        break;
                    case 117110:
                        if (arch.equals("x86")) {
                            var3 = 0;
                        }
                        break;
                    case 3178856:
                        if (arch.equals("i386")) {
                            var3 = 2;
                        }
                        break;
                    case 3181739:
                        if (arch.equals("i686")) {
                            var3 = 1;
                        }
                        break;
                    case 3222903:
                        if (arch.equals("ia32")) {
                            var3 = 3;
                        }
                        break;
                    case 93084186:
                        if (arch.equals("arm64")) {
                            var3 = 11;
                        }
                        break;
                    case 93086174:
                        if (arch.equals("armv8")) {
                            var3 = 10;
                        }
                        break;
                    case 145444210:
                        if (arch.equals("armeabi-v7a")) {
                            var3 = 8;
                        }
                        break;
                    case -1409295794:
                        if(arch.equals("armv8l")) {
                            var3 = 9;
                        }
                        break;
                }

                switch (var3) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        sRuntimeAbi = "x86";
                        break;
                    case 4:
                    case 5:
                        if (is64bitDevice()) {
                            sRuntimeAbi = "x86_64";
                        } else {
                            sRuntimeAbi = "x86";
                        }
                        break;
                    case 6:
                    case 7:
                    case 8:
                        sRuntimeAbi = "armeabi-v7a";
                        break;
                    case 9:
                    case 10:
                    case 11:
                        if (is64bitDevice()) {
                            sRuntimeAbi = "arm64-v8a";
                        } else {
                            sRuntimeAbi = "armeabi-v7a";
                        }
                        break;
                    default:
                        sRuntimeAbi = "unknown";
                        break;
                }
            }

            if (sRuntimeAbi.equals("armeabi-v7a")) {
                if (isIaDevice()) {
                    sRuntimeAbi = "x86";
                }
            } else if (sRuntimeAbi.equals("arm64-v8a") && isIaDevice()) {
                sRuntimeAbi = "x86_64";
            }

            Log.d("XWalkLib", "Runtime ABI: " + sRuntimeAbi);
        }

        return sRuntimeAbi;
    }

    private static boolean isIaDevice() {
        String abi = getDeviceAbi();
        if(null == abi)
            return false;
        return abi.equals("x86") || abi.equals("x86_64");
    }

    public static class MemoryInfo {

        String processName;
        int pid;
        int uid;
        /**
         * 单位是KB
         */
        int memorySize;


    }

    public static int getCurrentMenmoryInfo() {
        List<MemoryInfo> infoList = getMemory(getApplication());
        for (MemoryInfo memoryInfo : infoList) {
            if (getApplication().getPackageName().equals(memoryInfo.processName)) {
                return memoryInfo.memorySize;
            }
        }
        return 0;
    }

    public static List<MemoryInfo> getMemory(Context context) {
        List<MemoryInfo> infoList = new ArrayList<MemoryInfo>();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);// 获得系统里正在运行的所有进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessesList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessesList) {
            MemoryInfo mInfo = new MemoryInfo();
            mInfo.pid = runningAppProcessInfo.pid;// 进程ID号
            mInfo.uid = runningAppProcessInfo.uid;// 用户ID
            mInfo.processName = runningAppProcessInfo.processName;// 进程名
            int[] pids = new int[] { mInfo.pid };
            Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);// 占用的内存
            int memorySize = memoryInfo[0].dalvikPrivateDirty;
            mInfo.memorySize = memorySize;
            infoList.add(mInfo);
        }
        return infoList;
    }
    //获取总內存大小
    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() / 1000;// 获得系统总内存，单位是MB
            localBufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return initial_memory;
    }

    private static boolean isMoreThan6Inch(Activity activity) {
        WindowManager wm = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        float screenWidth = (float)display.getWidth();
        float screenHeight = (float)display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow((double)((float)dm.widthPixels / dm.xdpi), 2.0D);
        double y = Math.pow((double)((float)dm.heightPixels / dm.ydpi), 2.0D);
        double screenInches = Math.sqrt(x + y);
        return screenInches >= 6.0D;
    }

    public static boolean isScreenSizeLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }
}
