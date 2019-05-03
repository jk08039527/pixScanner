package com.jerry.pixscanner.utils;

import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.jerry.pixscanner.MyApplication;

import static android.os.Looper.getMainLooper;

/**
 * 自定义的Toast工具类，为了避免用户不间断点击 也可以显示自定义的Toast
 *
 * @author my
 * @time 2016/9/22 14:44
 */
public class ToastUtil {

    private static Toast toast = null;

    /**
     * 显示Toast提示
     *
     * @param s 字符串内容
     * @param duration 显示时间
     */
    private static void showText(String s, int duration) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        if (toast != null) {
            toast.cancel();
        }

        //环境变量不为空
        toast = Toast.makeText(MyApplication.getInstance(), s, duration);
        toast.show();
    }

    /**
     * 显示Toast提示，显示时间默认为Toast.LENGTH_SHORT
     *
     * @param resId 字符串资源Id
     */
    public static void showShortText(int resId) {
        showShortText(MyApplication.getInstance().getResources().getString(resId));
    }

    /**
     * 显示Toast提示，显示时间默认为Toast.LENGTH_LONG
     *
     * @param s 字符串内容
     */
    public static void showShortText(String s) {
        if (checkThread()) {
            showText(s, Toast.LENGTH_SHORT);
        } else {
            new WeakHandler(getMainLooper()).post(() -> showShortText(s));
        }
    }

    /**
     * 显示Toast提示，显示时间默认为Toast.LENGTH_LONG
     *
     * @param resId 字符串资源Id
     */
    public static void showLongText(int resId) {
        showLongText(MyApplication.getInstance().getResources().getString(resId));
    }

    /**
     * 显示Toast提示，显示时间默认为Toast.LENGTH_LONG
     *
     * @param s 字符串内容
     */
    public static void showLongText(String s) {
        if (checkThread()) {
            showText(s, Toast.LENGTH_LONG);
        } else {
            new WeakHandler(getMainLooper()).post(() -> showShortText(s));
        }
    }


    public static boolean checkThread() {
        return Looper.myLooper() == getMainLooper();
    }
}
