package com.jerry.pixscanner.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jerry.pixscanner.LogUtils;
import com.jerry.pixscanner.MyApplication;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理类
 *
 * @author Tina
 */
public class FileUtil {

    public static File getSaveFile() {
        return new File(MyApplication.getInstance().getFilesDir(), "pic.jpg");
    }

    /**
     * 获取本地所有的图片
     *
     * @return list
     */
    public static List<String> getAllLocalPhotos() {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = MyApplication.getInstance().getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) {
            return null; // 没有图片
        }
        while (cursor.moveToNext()) {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists()) {
                result.add(path);
                LogUtils.i(path);
            }
        }

        return result;
    }

    /**
     * 清空文件：参数为文件夹时，只清理其内部文件，不清理本身, 参数为文件时，删除
     */
    public static void clearFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    clearFile(f);
                } else {
                    f.delete();
                }
            }
        } else {
            file.delete();
        }
    }

    public static void close(final Closeable... closeables) {
        try {
            for (Closeable closeable : closeables) {
                if (closeable == null) {
                    continue;
                }
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
