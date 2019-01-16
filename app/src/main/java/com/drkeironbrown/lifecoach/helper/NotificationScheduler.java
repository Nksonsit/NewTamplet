package com.drkeironbrown.lifecoach.helper;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.drkeironbrown.lifecoach.R;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationScheduler {
    public static final int DAILY_REMINDER_REQUEST_CODE = 100;
    public static final String TAG = "NotificationScheduler";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showNotificationOreo(Context context, Class<?> cls, String title, String content, boolean isMsg) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String id = "LifeCoach";
        @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_MAX);
        mChannel.setDescription(content);
        mChannel.enableLights(true);
        notificationManager.createNotificationChannel(mChannel);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        if (isMsg) {
            notificationIntent.putExtra("msg", content);
        }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,new Random().nextInt(),notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "id_product")
                .setSmallIcon(getNotificationIcon()) //your app icon
                .setBadgeIconType(getNotificationIcon()) //your app icon
                .setChannelId(id)
                .setContentTitle(title)
                .setSound(alarmSound)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setContentText(content)
                .setWhen(System.currentTimeMillis());
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

    }

    public static void showNotification(Context context, Class<?> cls, String title, String content, boolean isMsg) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        if (isMsg) {
            notificationIntent.putExtra("msg", content);
        }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,new Random().nextInt(),notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "id_product")
                .setSmallIcon(getNotificationIcon()) //your app icon
                .setBadgeIconType(getNotificationIcon()) //your app icon
                .setContentTitle(title)
                .setSound(alarmSound)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setContentText(content)
                .setWhen(System.currentTimeMillis());
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showNotificationOreo(Context context, Class<?> cls, String title, String content, int id, boolean isGallery) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String idd = "LifeCoach";
        @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(idd, title, NotificationManager.IMPORTANCE_MAX);
        mChannel.setDescription(content);
        mChannel.enableLights(true);
        notificationManager.createNotificationChannel(mChannel);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        if (isGallery) {
            notificationIntent.putExtra("galleryId", id);
        } else {
            notificationIntent.putExtra("slideshowId", id);
        }
        Log.e("notification", id + " " + content);
        notificationIntent.putExtra("title", title);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,new Random().nextInt(),notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "id_product")
                .setSmallIcon(getNotificationIcon()) //your app icon
                .setBadgeIconType(getNotificationIcon()) //your app icon
                .setContentTitle(title)
                .setSound(alarmSound)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setContentText(content + " ready to be viewed")
                .setWhen(System.currentTimeMillis())
                .setChannelId(idd);
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

    }

    public static void showNotification(Context context, Class<?> cls, String title, String content, int id, boolean isGallery) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        if (isGallery) {
            notificationIntent.putExtra("galleryId", id);
        } else {
            notificationIntent.putExtra("slideshowId", id);
        }
        Log.e("notification", id + " " + content);
        notificationIntent.putExtra("title", title);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,new Random().nextInt(),notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "id_product")
                .setSmallIcon(getNotificationIcon()) //your app icon
                .setBadgeIconType(getNotificationIcon()) //your app icon
                .setContentTitle(title)
                .setSound(alarmSound)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setContentText(content + " ready to be viewed")
                .setWhen(System.currentTimeMillis());
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher_round : R.mipmap.ic_launcher_round;
    }
}
