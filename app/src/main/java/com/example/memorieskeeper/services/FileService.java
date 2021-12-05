package com.example.memorieskeeper.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.memorieskeeper.R;

public class FileService extends Service {
    final int NOTIFICATION_ID = 1;
    final String CHANNEL_ID = "main";

    private NotificationManager notificationManager;
    private final IBinder binder = new FileServiceBinder();

    public class FileServiceBinder extends Binder {
        public FileService getService() {
            return FileService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        return binder;
    }

    public String uploadFile() {
        return "https://i.natgeofe.com/n/3861de2a-04e6-45fd-aec8-02e7809f9d4e/02-cat-training-NationalGeographic_1484324_square.jpg";
    }

    @Override
    public void onDestroy() {
        hideNotification();
        super.onDestroy();
    }

    public void showNotification() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "main", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification.Builder fileServiceNotificationBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("File Service")
                .setContentText("You're currently using the file service to upload images for your memory.")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light);
        Notification fileServiceNotification = fileServiceNotificationBuilder.build();
        notificationManager.notify(NOTIFICATION_ID, fileServiceNotification);
    }

    private void hideNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
