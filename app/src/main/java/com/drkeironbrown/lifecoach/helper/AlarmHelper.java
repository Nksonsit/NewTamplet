package com.drkeironbrown.lifecoach.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.model.Gallery;
import com.drkeironbrown.lifecoach.model.Slideshow;
import com.drkeironbrown.lifecoach.ui.Dashboard2Activity;
import com.drkeironbrown.lifecoach.ui.GalleryActivity;
import com.drkeironbrown.lifecoach.ui.GalleryListActivity;
import com.drkeironbrown.lifecoach.ui.SlideshowActivity;
import com.drkeironbrown.lifecoach.ui.SlideshowListActivity;

import java.io.CharArrayReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by anish on 27-03-2017.
 */

public class AlarmHelper extends BroadcastReceiver {
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("HEy", "onReceive: ");
        if (intent.getBooleanExtra("isImage", false)) {
            /*if (intent.getBooleanExtra("isGallery", true)) {
                Intent fireIntent = new Intent(context, GalleryActivity.class);
                fireIntent.putExtra("title", intent.getStringExtra("title"));
                fireIntent.putExtra("galleryId", intent.getIntExtra("galleryId", 0));
                context.startActivity(fireIntent);
            } else {
                Intent fireIntent = new Intent(context, SlideshowActivity.class);
                fireIntent.putExtra("title", intent.getStringExtra("title"));
                fireIntent.putExtra("slideshowId", intent.getIntExtra("slideshowId", 0));
                context.startActivity(fireIntent);
            }*/
        }

        if (intent.getBooleanExtra("IsInspirational", false)) {

            int random = 0;
            AlarmHelper alarmHelper = new AlarmHelper();
            if (PrefUtils.getInspirationalNotiTime(context) != null) {
                String tempTime = PrefUtils.getInspirationalNotiTime(context);
                String[] timeSplit = tempTime.split(":");
                alarmHelper.setReminder(context, AppConstant.INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), true, true);
            } else {
                final int min = 9;
                final int max = 21;
                random = new Random().nextInt((max - min) + 1) + min;
                alarmHelper.setReminder(context, AppConstant.INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, random, 0, true, true);
            }

/*            Intent fireIntent = new Intent(context, Dashboard2Activity.class);
            fireIntent.putExtra("msg", intent.getStringExtra("msg"));
            context.startActivity(fireIntent);*/

            NotificationScheduler.showNotification(context, Dashboard2Activity.class, "New Inspirational message",
                    intent.getStringExtra("msg").replace("&#44;", ",").replace("&#39;", "'").replace("&#34;", "\""));

        }
        if (intent.getBooleanExtra("IsPInspirational", false)) {
            int random = 0;
            AlarmHelper alarmHelper = new AlarmHelper();
            if (PrefUtils.getPInspirationalNotiTime(context) != null) {
                String tempTime = PrefUtils.getPInspirationalNotiTime(context);
                String[] timeSplit = tempTime.split(":");
                alarmHelper.setReminder(context, AppConstant.P_INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), true, false);
            } else {
                final int min = 9;
                final int max = 21;
                random = new Random().nextInt((max - min) + 1) + min;
                alarmHelper.setReminder(context, AppConstant.P_INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, random, 0, true, false);
            }

/*            Intent fireIntent = new Intent(context, Dashboard2Activity.class);
            fireIntent.putExtra("msg", intent.getStringExtra("msg"));
            context.startActivity(fireIntent);*/

            NotificationScheduler.showNotification(context, Dashboard2Activity.class, "New personal inspirational message",
                    intent.getStringExtra("msg").replace("&#44;", ",").replace("&#39;", "'").replace("&#34;", "\""));

        }
    }

    public void setReminder(Context context, Date date, int calId, String title, String subTitle) {
        cancelAlarm(context, calId);
        if (date == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.e("calendar time", "" + calendar);
        PendingIntent pendingIntent;

        Intent intent = new Intent(context, AlarmHelper.class);
//        intent.putExtra(UtilConstants.INTENT_TITLE, title);
//        intent.putExtra(UtilConstants.INTENT_SUBTITLE, subTitle);

        pendingIntent = PendingIntent.getBroadcast(context, calId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = getAlarmManager(context);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void setReminder(Context context, int calId, Class<?> cls, int day, int month, int year, int hour, int min, boolean isGallery, Object obj) {
        cancelAlarm(context, calId);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date date = cal.getTime();

        if (date == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.e("calendar time", "" + calendar);
        PendingIntent pendingIntent;


        Intent intent = new Intent(context, AlarmHelper.class);
        if (isGallery) {
            intent.putExtra("title", ((Gallery) obj).getGalleryName());
            intent.putExtra("galleryId", ((Gallery) obj).getGalleryId());
            intent.putExtra("isImage", true);
        } else {
            intent.putExtra("title", ((Slideshow) obj).getSlideshowName());
            intent.putExtra("slideshowId", ((Slideshow) obj).getSlideshowId());
            intent.putExtra("isImage", true);
        }
        intent.putExtra("isGallery", isGallery);
        pendingIntent = PendingIntent.getBroadcast(context, calId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = getAlarmManager(context);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void setReminder(Context context, int calId, Class<?> cls, int hour, int min, boolean isNext, boolean isInspirational) {
        cancelAlarm(context, calId);

        Calendar cal = Calendar.getInstance();
        if (isNext) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date date = cal.getTime();

        if (date == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.e("calendar time", "" + calendar);
        PendingIntent pendingIntent;

        if (isInspirational) {
            Intent intent = new Intent(context, AlarmHelper.class);
            intent.putExtra("msg", DBOpenHelper.getRandomInspirational().getInspirational());
            intent.putExtra("IsInspirational", true);
            pendingIntent = PendingIntent.getBroadcast(context, calId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Intent intent = new Intent(context, AlarmHelper.class);
            intent.putExtra("msg", DBOpenHelper.getRandomPInspirational().getPInspirational());
            intent.putExtra("IsPInspirational", true);
            pendingIntent = PendingIntent.getBroadcast(context, calId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        AlarmManager alarmManager = getAlarmManager(context);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void cancelAlarm(Context context, long alarmID) { // to cancel the alarm of a specific id.
        Intent intent = new Intent(context, AlarmHelper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarmID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = getAlarmManager(context);
        alarmManager.cancel(pendingIntent);
    }

    private AlarmManager getAlarmManager(Context context) { //using the alarm service of device

        if (this.alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return alarmManager;
    }
}