package com.example.nsity.schooldiary.navigation.timetable.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.nsity.schooldiary.navigation.timetable.Timetable;
import com.example.nsity.schooldiary.navigation.timetable.TimetableItem;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nsity on 18.02.16.
 */
public class TimetableNotificationIntentService extends IntentService {

    public static final String TIMETABLE_EXTRA = "timetable";
    public static final String NOTIFICATION_SETTING = "notification_setting";

    private SimpleDateFormat fromTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private SimpleDateFormat fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.ENGLISH);
    private SimpleDateFormat minute = new SimpleDateFormat("mm", Locale.ENGLISH);


    public TimetableNotificationIntentService() {
        super("TimetableNotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getBooleanExtra(NOTIFICATION_SETTING, false))
            setAlarmReceiver();
        else
            clearAlarmReceiver();
    }


    private void setAlarmReceiver() {
        Timetable timetable = new Timetable(this);
        if (timetable.getTimetable().size() == 0)
            return;

        clearAlarmReceiver();

       // if(Preferences.get(Preferences.NOTIFICATION_LAST_SYNC, this).equals(""))
         //   Preferences.set(Preferences.NOTIFICATION_LAST_SYNC, CommonFunctions.getCurrentDateyyyyMMddHHmmss(), this);

        /*int i = 0;
        for (TimetableItem timetableItem : timetable.getTimetable()) {
            Intent intent = new Intent(this, TimetableNotificationReceiver.class);
            intent.putExtra(TIMETABLE_EXTRA, timetableItem);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, getDay(timetableItem.getDay()));
            calendar.set(Calendar.HOUR, Integer.valueOf(getHour(timetableItem.getTime().getTimeStart())));
            calendar.set(Calendar.MINUTE, Integer.valueOf(getMinute(timetableItem.getTime().getTimeStart())));
            calendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

            i++;
        }*/


        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int numDays = Calendar.getInstance().getActualMaximum(Calendar.DATE);

        for (int i = dayOfMonth; i <= numDays; i++) {
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int year = Calendar.getInstance().get(Calendar.YEAR);

            try {
                Date selectedDate = fromDate.parse(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(i));
                int dayOfWeek = CommonFunctions.getDayOfWeek(selectedDate);

                if(timetable.getTimetableOfDay(dayOfWeek) != null) {
                    int k = 0;
                    for (TimetableItem timetableItem : timetable.getTimetableOfDay(dayOfWeek)) {
                        Intent intent = new Intent(this, TimetableNotificationReceiver.class);
                        intent.putExtra(TIMETABLE_EXTRA, timetableItem);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, k, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, i);
                        calendar.set(Calendar.MONTH, month - 1);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.HOUR, Integer.valueOf(getHour(timetableItem.getTime().getTimeStart())));
                        calendar.set(Calendar.MINUTE, Integer.valueOf(getMinute(timetableItem.getTime().getTimeStart())));
                        calendar.set(Calendar.SECOND, 0);

                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        if (android.os.Build.VERSION.SDK_INT >= 19) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                        k++;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*for (CheckPoint checkPoint : checkPoints) {
            Intent intent = new Intent(context, CheckAlarmReceiver.class);
            intent.putExtra("checkPoint", checkPoint);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(getDay(checkPoint.getDate())));
            calendar.set(Calendar.MONTH, Integer.valueOf(getMonth(checkPoint.getDate())) - 1);
            calendar.set(Calendar.YEAR, Integer.valueOf(getYear(checkPoint.getDate())));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(getHour(checkPoint.getCheckInTime())));
            calendar.set(Calendar.MINUTE, Integer.valueOf(getMinute(checkPoint.getCheckInTime())));
            calendar.set(Calendar.SECOND, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

            i++;
        }*/


        Preferences.set(Preferences.NOTIFICATION_LAST_SYNC, CommonFunctions.getCurrentDateyyyyMMdd(), this);
    }


    private void clearAlarmReceiver() {
        for (int i = 0; i < 800; i++) {
            Intent intent = new Intent(this, TimetableNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private String getMinute(String baseTime) {
        try {
            Date date = fromTime.parse(baseTime);
            return minute.format(date);
        } catch (Exception e) {
            return baseTime;
        }
    }

    private String getHour(String baseTime) {
        try {
            Date date = fromTime.parse(baseTime);
            return hour.format(date);
        } catch (Exception e) {
            return baseTime;
        }
    }
}
