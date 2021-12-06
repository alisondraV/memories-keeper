package com.example.memorieskeeper.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.example.memorieskeeper.AddMemoryActivity;
import com.example.memorieskeeper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class FileService extends Service {
    final int NOTIFICATION_ID = 1;
    final String CHANNEL_ID = "main";

    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();

        Uri imageUri = intent.getData();
        uploadImageToFirebase(imageUri);

        return super.onStartCommand(intent, flags, startId);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(0);
                FirebaseStorage.getInstance()
                        .getReference(imageUri.getLastPathSegment())
                        .putFile(imageUri)
                        .addOnCompleteListener(task -> {
                            stopSelf();
                            hideNotification();
                        });
            } catch (InterruptedException e) {
                stopSelf();
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
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
                .setContentTitle("Creating memory...")
                .setContentText("You're currently using the file service to upload images for your memory.")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light);
        Notification fileServiceNotification = fileServiceNotificationBuilder.build();
        notificationManager.notify(NOTIFICATION_ID, fileServiceNotification);
    }

    private void hideNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
