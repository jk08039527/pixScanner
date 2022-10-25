package com.jerry.pixscanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class MediaService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID = "com.jerry.pixscanner.MediaService";
    private static final String NOTIFICATION_CHANNEL_NAME = "com.jerry.pixscanner.channel_name";
    private static final String NOTIFICATION_CHANNEL_DESC = "com.jerry.pixscanner.channel_desc";

    public MediaService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startNotification();
    }

    public void startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Call Start foreground with notification
            Intent notificationIntent = new Intent(this, MediaService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Starting Service")
                .setContentText("Starting monitoring service")
                .setContentIntent(pendingIntent);
            Notification notification = notificationBuilder.build();
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            startForeground(1, notification); //必须使用此方法显示通知，不能使用notificationManager.notify，否则还是会报上面的错误
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyMediaService(MediaService.this);
    }

    public static class MyMediaService extends Binder {

        private final MediaService mMediaService;

        public MyMediaService(final MediaService mediaService) {
            mMediaService = mediaService;
        }

        public MediaService getService() {
            return mMediaService;
        }
    }
}