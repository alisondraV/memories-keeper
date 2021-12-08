package com.example.memorieskeeper.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.example.memorieskeeper.MemoryModel;
import com.example.memorieskeeper.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MemoryUploadService extends Service {
    public static final String MEMORY_PARAM = "memory";
    final int NOTIFICATION_PROGRESS_ID = 1;
    final int NOTIFICATION_SUCCESS_ID = 2;
    final int NOTIFICATION_ERROR_ID = -1;
    final String CHANNEL_ID = "main";

    private NotificationManager notificationManager;
    private MemoryModel memory;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "main", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);
        showUploadNotification();

        Uri imageUri = intent.getData();
        memory = intent.getParcelableExtra(MEMORY_PARAM);
        uploadImageToFirebase(imageUri);

        return super.onStartCommand(intent, flags, startId);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        Thread thread = new Thread(() -> {
            if (imageUri != null) {
                UploadTask uploadTask = FirebaseStorage.getInstance()
                        .getReference(imageUri.getLastPathSegment())
                        .putFile(imageUri);
                uploadTask
                        .addOnSuccessListener(snapshot -> {
                            FirebaseStorage.getInstance()
                                    .getReference(imageUri.getLastPathSegment())
                                    .getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        memory.setImageUrl(uri.toString());
                                        createRTDBDocument(memory);
                                    });
                        })
                        .addOnFailureListener(this::handleException);
            } else {
                createRTDBDocument(memory);
            }
        });
        thread.start();
    }

    private void createRTDBDocument(MemoryModel memory) {
        FirebaseDatabase.getInstance()
                .getReference(getString(R.string.memories_collection_name))
                .child(UUID.randomUUID().toString())
                .setValue(memory)
                .addOnSuccessListener(o -> showSuccessNotification())
                .addOnFailureListener(this::handleException);
    }

    public void handleException(Exception e) {
        showErrorNotification(e.getMessage());
        stopSelf();
    }

    public void showUploadNotification() {
        showNotification(
                NOTIFICATION_PROGRESS_ID,
                getString(R.string.notification_memory_progress_title),
                getString(R.string.notification_memory_progress_description)
        );
    }

    public void showErrorNotification(String message) {
        showNotification(NOTIFICATION_ERROR_ID, getString(R.string.notification_memory_error_title), message);
    }

    public void showSuccessNotification() {
        showNotification(
                NOTIFICATION_SUCCESS_ID,
                getString(R.string.notification_memory_success_title),
                getString(R.string.notification_memory_success_description)
        );
    }

    public void showNotification(int id, String title, String description) {
        Notification.Builder fileServiceNotificationBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light);
        Notification fileServiceNotification = fileServiceNotificationBuilder.build();
        notificationManager.notify(id, fileServiceNotification);
    }
}
