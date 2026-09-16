package com.example.aiwoodyassistant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class CaptureService extends Service {
    @Override public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel c = new NotificationChannel("woody_capture","AI Woody screen capture",
                    NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(c);
        }
        Notification.Builder b = Build.VERSION.SDK_INT >= 26 ?
                new Notification.Builder(this,"woody_capture") : new Notification.Builder(this);
        b.setContentTitle("AI Woody Assistant").setContentText("Screen capture is active").setSmallIcon(android.R.drawable.ic_menu_view);
        startForeground(7,b.build());
    }
    @Override public int onStartCommand(Intent intent,int flags,int id) {
        // The MediaProjection token is intentionally not processed into game-control actions.
        return START_STICKY;
    }
    @Override public IBinder onBind(Intent i){ return null; }
}
