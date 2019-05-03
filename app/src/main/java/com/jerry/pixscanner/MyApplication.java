package com.jerry.pixscanner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;

import com.jerry.pixscanner.flow.FloatItem;
import com.jerry.pixscanner.flow.FloatLogoMenu;
import com.jerry.pixscanner.flow.FloatMenuView;

import java.util.ArrayList;

/**
 * @author Jerry
 * @createDate 2019/4/10
 * @copyright www.aniu.tv
 * @description
 */
public class MyApplication extends Application {

    private FloatLogoMenu menu;
    @SuppressLint("StaticFieldLeak")
    private static Application mInstance;

    public static Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInstance.startService(new Intent(mInstance, ListenerService.class));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(mInstance)) {
            ArrayList<FloatItem> itemList = new ArrayList<>();
            itemList.add(new FloatItem("开始", 0x99000000, 0x99000000, BitmapFactory.decodeResource(mInstance.getResources(), R.drawable.play), "0"));
            menu = new FloatLogoMenu.Builder()
                    .withContext(this)//这个在7.0（包括7.0）以上以及大部分7.0以下的国产手机上需要用户授权，需要搭配<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
                    .logo(BitmapFactory.decodeResource(mInstance.getResources(), R.drawable.menu))
                    .drawCicleMenuBg(true)
                    .backMenuColor(0xffe4e3e1)
                    .setBgDrawable(mInstance.getResources().getDrawable(R.drawable.main_menu))
                    //这个背景色需要和logo的背景色一致
                    .setFloatItems(itemList)
                    .defaultLocation(FloatLogoMenu.RIGHT)
                    .drawRedPointNum(false)
                    .showWithListener(new FloatMenuView.OnMenuClickListener() {
                        @Override
                        public void onItemClick(int position, String title) {
                            switch (position) {
                                case 0:
                                    break;
                                case 1:
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void dismiss() {

                        }
                    });
        }
    }
}
