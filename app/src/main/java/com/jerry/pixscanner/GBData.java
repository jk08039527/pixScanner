package com.jerry.pixscanner;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;

/**
 * @author Jerry
 * @createDate 2019/4/14
 * @copyright www.aniu.tv
 * @description
 */
public class GBData {

    static ImageReader reader;
    private static Bitmap bitmap;

    /**
     * 获取目标点的RGB值
     */
    public static int getColor(int x, int y) {
        if (reader == null) {
            LogUtils.w("reader is null");
            return -1;
        }

        Image image = reader.acquireLatestImage();

        if (image == null) {
            if (bitmap == null) {
                LogUtils.w("image is null");
                return -1;
            }
            return bitmap.getPixel(x, y);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        }
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        return bitmap.getPixel(x, y);
    }
}
