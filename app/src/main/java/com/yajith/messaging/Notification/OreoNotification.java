package com.yajith.messaging.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

public class OreoNotification extends ContextWrapper {
    private static final String channel="com.yajith.messaging";
    private static final String channel_name="messaging";
    private NotificationManager notificationManager;
    public OreoNotification(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel notificationChannel=new NotificationChannel(channel,channel_name,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

    }
    public NotificationManager getNotificationManager()
    {
        if(notificationManager!=null)
        {
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getNotificationbuilder(String title, String body, Uri sound, PendingIntent pendingIntent,String icon)
    {
        return new Notification.Builder(getApplicationContext(),channel).setContentTitle(title).setContentText(body).setSmallIcon(Integer.parseInt(icon)).setSound(sound).setAutoCancel(true);
    }
}
