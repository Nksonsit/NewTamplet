package com.drkeironbrown.lifecoach.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.drkeironbrown.lifecoach.ui.GalleryListActivity;


public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.e(TAG, "onReceive: BOOT_COMPLETED");
//                NotificationScheduler.setReminder(context, AlarmReceiver.class, hour, min);
                return;
            }
        }

        Log.e(TAG, "onReceive: ");

        //Trigger the notification
/*        NotificationScheduler.showNotification(context, GalleryListActivity.class,
                "You have 5 unwatched videos", "Watch them now?");*/

    }
}