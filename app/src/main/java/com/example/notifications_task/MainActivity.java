package com.example.notifications_task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat manager;
    public static final String NORMAL_CHANNEL = "NORMAL_CHANNEL";
    public static final String IMPORTANT_CHANNEL = "IMPORTANT_CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            {
                String name = getResources().getString(R.string.NOT_IMPORTANT_CHANNEL_NAME);
                NotificationChannel channel = new NotificationChannel(
                        NORMAL_CHANNEL,
                        name,
                        NotificationManager.IMPORTANCE_LOW
                );
                String description = getResources().getString(R.string.NOT_IMPORTANT_CHANNEL_DESCRIPTION);
                channel.setDescription(description);
                channel.enableVibration(false);
                manager.createNotificationChannel(channel);
            }
        }
    }

    public void simpleNotification(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);
        builder.setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle("Простое оповещение")
                .setContentText("Что-то важное произошло");

        builder.setLargeIcon(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background)
        );

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.notify(R.id.SIMPLE_NOTIFICATION_ID, builder.build());
    }

    public void simpleCancel(View view) {
        manager.cancel(R.id.SIMPLE_NOTIFICATION_ID);
    }

    public void browserNotification(View view) {
        Intent intent = new Intent(this, browser.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, R.id.BROWSER_PENDING_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);
        builder.setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle("Запустить браузер")
                .setContentText("Текст оповещения");

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.notify(R.id.GOOGLE_NOTIFICATION_ID, builder.build());
    }

    public void complexNotification(View view) {
        Intent browser = new Intent(Intent.ACTION_VIEW);
        browser.setData(Uri.parse("https://louvre.fr"));
        PendingIntent browserPI = PendingIntent.getActivity(
                this,
                R.id.BROWSER_PENDING_ID,
                browser,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        Intent map = new Intent(Intent.ACTION_VIEW);
        map.setData(Uri.parse("geo:48.85,2.34"));
        PendingIntent mapPI = PendingIntent.getActivity(
                this,
                R.id.MAP_PENDING_ID,
                map,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NORMAL_CHANNEL);

        builder.setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle("Сложное уведомление")
                .setContentText("Описание уведомления");

        builder.addAction(new NotificationCompat.Action(
                android.R.drawable.btn_star,
                "В браузере",
                browserPI
        ));
        builder.addAction(new NotificationCompat.Action(
                android.R.drawable.btn_star,
                "На карте",
                mapPI
        ));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.notify(R.id.GOOGLE_NOTIFICATION_ID, builder.build());
    }
}