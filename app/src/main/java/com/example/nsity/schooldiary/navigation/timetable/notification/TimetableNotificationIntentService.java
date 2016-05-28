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
import java.text.ParseException;
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

        int i = 0;
        for (TimetableItem timetableItem : timetable.getTimetable()) {
            Intent intent = new Intent(this, TimetableNotificationReceiver.class);
            intent.putExtra(TIMETABLE_EXTRA, timetableItem);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, getDay(timetableItem.getDay()));

            try {
                Date date = fromTime.parse(timetableItem.getTime().getTimeStart());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MINUTE, -5);

                String dateWithoutFiveMinutes = fromTime.format(cal.getTime());

                calendar.set(Calendar.HOUR, Integer.valueOf(getHour(dateWithoutFiveMinutes)));
                calendar.set(Calendar.MINUTE, Integer.valueOf(getMinute(dateWithoutFiveMinutes)));
                calendar.set(Calendar.SECOND, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                long timeToAlarm = calendar.getTimeInMillis();
                if (calendar.getTimeInMillis() < System.currentTimeMillis())
                {
                    timeToAlarm += (24 * 60 * 60 * 1000) * 7;
                }


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToAlarm, AlarmManager.INTERVAL_DAY * 7, pendingIntent);


            } catch (ParseException e) {
                e.printStackTrace();
            }

            i++;
        }

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

    private int getDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                dayOfWeek++;
                break;
            case 7:
                dayOfWeek = 1;
                break;
        }
        return dayOfWeek;
    }
}
