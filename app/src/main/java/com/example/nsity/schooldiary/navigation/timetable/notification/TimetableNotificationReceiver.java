package com.example.nsity.schooldiary.navigation.timetable.notification;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;


/**
 * Created by nsity on 18.02.16.
 */
public class TimetableNotificationReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, ShowNotificationWakefulService.class).
                putExtra(TimetableNotificationIntentService.TIMETABLE_EXTRA,
                        intent.getSerializableExtra(TimetableNotificationIntentService.TIMETABLE_EXTRA));
        startWakefulService(context, service);
    }
}
