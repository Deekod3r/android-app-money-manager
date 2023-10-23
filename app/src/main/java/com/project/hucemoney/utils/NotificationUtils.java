package com.project.hucemoney.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.project.hucemoney.R;

public class NotificationUtils {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "HUCE Money";

    public static void showNotification(Context context, String title, String message) {
        if (hasNotificationPermission(context)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "HUCE Money", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_app, 500)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Notification notification = builder.build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            requestNotificationPermission(context);
        }
    }

    private static boolean hasNotificationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestNotificationPermission(Context context) {
        if (context instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, NOTIFICATION_ID);
        } else {
            Toast.makeText(context, "Yêu cầu quyền thông báo", Toast.LENGTH_SHORT).show();
        }
    }

    public static void handleNotificationPermissionResponse(Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NOTIFICATION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showNotification(context, "Thông báo", "Chào mừng bạn đến với ứng dụng của chúng tôi!");
            } else {
                Toast.makeText(context, "Quyền thông báo bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isNotificationPermissionGranted(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        return notificationManager.areNotificationsEnabled();
    }

}


