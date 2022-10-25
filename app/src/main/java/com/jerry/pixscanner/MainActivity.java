package com.jerry.pixscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mMediaProjectionManager != null) {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
        findViewById(R.id.img).setOnTouchListener((v, event) -> {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            if (action == MotionEvent.ACTION_DOWN) {
                int color = GBData.getColor((int) event.getX(), (int) event.getY());
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                LogUtils.d("X:" + event.getX() + ",Y:" + event.getY() + ":red:" + r + ",green:" + g + ",blue:" + b);
                Toast.makeText(MainActivity.this, "red:" + r + ",green:" + g + ",blue:" + b, Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                LogUtils.i("User cancelled");
                Toast.makeText(this, "User cancelled!", Toast.LENGTH_SHORT).show();
                return;
            }

            LogUtils.i("Starting screen capture");
            mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            setUpVirtualDisplay();
        }
    }

    private void setUpVirtualDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        final ImageReader imageReader = ImageReader.newInstance(dm.widthPixels, dm.heightPixels, PixelFormat.RGBA_8888, 1);
        mMediaProjection.createVirtualDisplay("ScreenCapture",
            dm.widthPixels, dm.heightPixels, dm.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.getSurface(), null, null);
        GBData.reader = imageReader;
    }

}
