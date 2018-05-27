package com.example.csar.kbc_news.actividades;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import com.example.csar.kbc_news.R;


public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        String p1 = "", p2 = "", p3 = "";
        if((null != intent) && (null != intent.getExtras())) {
            Bundle bundle = intent.getExtras();
            p1 = intent.getExtras().getString("TITULO");
            p2 = intent.getExtras().getString("DESCRIPCION");
            p3 = intent.getExtras().getString("URL");
        }

        Intent notificationIntent = new Intent(context, ActividadWeb.class);
        notificationIntent.putExtra("TITLE", p1);
        notificationIntent.putExtra("URL", p3);

        builder.setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, 0))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(p1)
                .setContentText(p2)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Zews");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }
}