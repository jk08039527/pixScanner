package com.jerry.pixscanner;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.jerry.pixscanner.utils.FileUtil;
import com.jerry.pixscanner.utils.ToastUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by cxk on 2017/2/4. email:471497226@qq.com
 * <p>
 * 获取即时微信聊天记录服务类
 */

public class ListenerService extends AccessibilityService {

    private static ListenerService instance;

    public static ListenerService getInstance() {
        return instance;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "我快被终结了啊-----", Toast.LENGTH_SHORT).show();
    }

    /**
     * 服务开始连接
     */
    @Override
    protected void onServiceConnected() {
        Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
        super.onServiceConnected();
    }

    /**
     * 服务断开
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "服务已被关闭", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    public void input(AccessibilityNodeInfo node, String text) {
        //粘贴板
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);

        CharSequence txt = node.getText();
        if (txt == null) {
            txt = "";
        }

        Bundle arguments = new Bundle();
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, txt.length());
        node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        node.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
    }

    public void back() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    @SuppressLint("DefaultLocale")
    public void exeSwip(int startX, int startY, int endX, int endY) {
        String swip = "input swipe %d %d %d %d %d";
        execShellCmd(String.format(swip, startX, startY, endX, endY, 500));
    }

    @SuppressLint("DefaultLocale")
    public void exeClick(AccessibilityNodeInfo node) {
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        String click = "input tap %d %d";
        execShellCmd(String.format(click, (rect.left + rect.right) >> 1, (rect.top + rect.bottom) >> 1));
    }

    @SuppressLint("DefaultLocale")
    public void exeClick(int x, int y) {
        String click = "input tap %d %d";
        execShellCmd(String.format(click, x, y));
    }

    private void execShellCmd(String cmd) {
        LogUtils.d(cmd);
        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            outputStream = process.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd + "\n");
            dataOutputStream.flush();
        } catch (IOException e) {
            ToastUtil.showShortText("请查看是否获取权限");
            e.printStackTrace();
        } finally {
            FileUtil.close(dataOutputStream, outputStream);
        }
    }
}