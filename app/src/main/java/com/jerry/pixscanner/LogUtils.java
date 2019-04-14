package com.jerry.pixscanner;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * Author：OTMAGIC
 * WeChat：Longalei888
 * Date：2018/5/31
 * Signature:每一个Bug修改,每一次充分思考,都会是一种进步.
 * Describtion: log日志
 */

public class LogUtils {
    //debug 日志输入开关
    @SuppressLint("DefaultLocale")
    private static String getTAG() {
        StackTraceElement element = new Throwable().getStackTrace()[2];
        String TAG = "%s.%s(L:%d)";
        String className = element.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        TAG = String.format(TAG, className, element.getMethodName(), element.getLineNumber());
        return BuildConfig.APPLICATION_ID + ":" + TAG;
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(getTAG(), msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(getTAG(), msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(getTAG(), msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(getTAG(), msg);
        }

    }

    public static void e(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

}
