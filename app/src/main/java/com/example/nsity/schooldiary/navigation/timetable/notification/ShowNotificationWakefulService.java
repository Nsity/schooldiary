package com.example.nsity.schooldiary.navigation.timetable.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Log;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.MainActivity;
import com.example.nsity.schooldiary.navigation.timetable.TimetableItem;
import com.example.nsity.schooldiary.system.CommonFunctions;

/**
 * Created by nsity on 20.02.16.
 */
public class ShowNotificationWakefulService extends IntentService {

    public ShowNotificationWakefulService() {
        super("ShowNotificationWakefulService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TimetableItem timetableItem = (TimetableItem) intent.getSerializableExtra(TimetableNotificationIntentService.TIMETABLE_EXTRA);

        Log.d("TAG", timetableItem.getTime().getTimeStart());

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        String ticker = "<b>" + timetableItem.getSubject().getName() + "</b>" + (timetableItem.getRoom().equals("") ? "" : " - " + "<i> каб. " + timetableItem.getRoom() + "</i>");

        String contentText = CommonFunctions.getTime(timetableItem.getTime().getTimeStart()) + " - " +
                CommonFunctions.getTime(timetableItem.getTime().getTimeEnd());

        String contentTitle = timetableItem.getSubject().getName() + (timetableItem.getRoom().equals("") ? "" : " (каб. " + timetableItem.getRoom() +  ")");

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_school_white_24dp)
                .setTicker(Html.fromHtml(ticker))
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.school), 150, 150, true))
                .setContentTitle(contentTitle)
                .setAutoCancel(true)
                .setContentText(contentText)
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);

        TimetableNotificationReceiver.completeWakefulIntent(intent);
    }
}