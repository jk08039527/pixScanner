package com.jerry.pixscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private MediaProjectionManager mMediaProjectionManager;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            LogUtils.i("Starting screen capture");
            mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            if (mMediaProjectionManager != null) {
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {

        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        bindService(new Intent(this, MediaService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
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
            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);

            final ImageReader imageReader = ImageReader.newInstance(dm.widthPixels, dm.heightPixels, PixelFormat.RGBA_8888, 1);
            mediaProjection.createVirtualDisplay("ScreenCapture",
                dm.widthPixels, dm.heightPixels, dm.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
            GBData.reader = imageReader;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
