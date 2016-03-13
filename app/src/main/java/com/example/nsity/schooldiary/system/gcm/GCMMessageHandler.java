package com.example.nsity.schooldiary.system.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.Preferences;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by nsity on 21.02.16.
 */
public class GCMMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        if(Preferences.getBoolean(Preferences.NOTIFICATION_MARK_SETTING, false, getBaseContext()))
            createNotification(message);
    }

    private void createNotification(String body) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setTicker(body)
                .setSmallIcon(R.mipmap.ic_school_white_24dp).setContentTitle(getString(R.string.app_name))
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.school), 150, 150, true))
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

}