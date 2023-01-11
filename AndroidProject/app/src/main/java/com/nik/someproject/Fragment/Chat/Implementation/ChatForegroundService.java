package com.nik.someproject.Fragment.Chat.Implementation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatForegroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String uhash = intent.getStringExtra("uhash");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.i("FGService", "FG service..." + uhash);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        createMainNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void createMainNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String CHANNEL_ID = "FG Socket SomeProject";
            final int SERVICE_ID = 1001;

            Intent userHide = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName())
                    .putExtra(Settings.EXTRA_CHANNEL_ID, SERVICE_ID);
            startActivity(userHide);

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_LOW
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);

            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setPriority(Notification.PRIORITY_MIN);
            startForeground(SERVICE_ID, notification.build());
        }
    }
}
