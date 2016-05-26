package com.example.nsity.schooldiary.system.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.lesson.LessonActivity;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.navigation.marks.Teachers;
import com.example.nsity.schooldiary.navigation.messages.ChatRoomActivity;
import com.example.nsity.schooldiary.navigation.messages.Message;
import com.example.nsity.schooldiary.navigation.messages.MessagesFragment;
import com.example.nsity.schooldiary.navigation.timetable.Timetable;
import com.example.nsity.schooldiary.navigation.timetable.TimetableItem;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.Utils;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 21.02.16.
 */
public class GCMMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    public static final String LESSON_COLLAPSE_KEY = "lesson";
    public static final String HOMEWORK_COLLAPSE_KEY = "homework";
    public static final String PROGRESS_COLLAPSE_KEY = "progress";
    public static final String MESSAGE_COLLAPSE_KEY = "message";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString(getString(R.string.message));
        String collapseKey = data.getString(getString(R.string.collapse_key));

        if(collapseKey == null) {
            return;
        }

        switch (collapseKey) {
            case LESSON_COLLAPSE_KEY:
                createLessonNotification(message);
                break;
            case PROGRESS_COLLAPSE_KEY:
                createProgressNotification(message);
                break;
            case MESSAGE_COLLAPSE_KEY:
                createMessageNotification(message);
                break;
        }
    }


    private TimetableItem getTimetableItemByTimeId(ArrayList<TimetableItem> arr, int timeId) {
        for (TimetableItem item: arr) {
            if(item.getTime().getId() == timeId) {
                return item;
            }
        }
        return null;
    }

    private void createLessonNotification(String body) {
        if(!Preferences.getBoolean(Preferences.NOTIFICATION_SETTING, false, getBaseContext())) {
            return;
        }

        Context context = getBaseContext();

        try {
            JSONObject jsonObject = new JSONObject(body);
            String day = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.day));
            int dayOfWeek = CommonFunctions.getFieldInt(jsonObject, context.getString(R.string.day_of_week));
            int timeId = CommonFunctions.getFieldInt(jsonObject, context.getString(R.string.time_id));
            TimetableItem timetableItem = getTimetableItemByTimeId(new Timetable(context).getTimetableOfDay(dayOfWeek), timeId);

            String text = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.text));


            Intent notificationIntent = new Intent(context, LessonActivity.class);
            notificationIntent.putExtra(Utils.TIMETABLE_ITEM, timetableItem);
            notificationIntent.putExtra(Utils.DAY, day);
            notificationIntent.putExtra("update", true);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            buildNotification(text, text, getString(R.string.app_name), contentIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void buildNotification(String tickerText, String contentText, String contentTitle, PendingIntent contentIntent) {
        if(!Preferences.getBoolean(Preferences.NOTIFICATION_SETTING, false, getBaseContext())) {
            return;
        }
        Context context = getBaseContext();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setTicker(tickerText)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_school_white_24dp)
                .setContentTitle(contentTitle)
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.school), 150, 150, true))
                .setContentText(contentText)
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }


    //TODO
    private void createProgressNotification(String body) {
        Context context = getBaseContext();

        try {
            JSONObject jsonObject = new JSONObject(body);
            String day = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.day));

            String text = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.text));


           /* Intent notificationIntent = new Intent(context, LessonActivity.class);
            notificationIntent.putExtra(Utils.TIMETABLE_ITEM, timetableItem);
            notificationIntent.putExtra(Utils.DAY, day);
            notificationIntent.putExtra("update", true);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);*/

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setTicker(text)
                    .setAutoCancel(true)
                  //  .setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_school_white_24dp).setContentTitle(getString(R.string.app_name))
                    .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.school), 150, 150, true))
                    .setContentText(text)
                    .setDefaults(Notification.DEFAULT_ALL);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void createMessageNotification(String body) {
        Context context = getBaseContext();

        try {
            JSONObject jsonObject = new JSONObject(body);

            String text = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.message_text));
            int teacherId = CommonFunctions.getFieldInt(jsonObject, context.getString(R.string.teacher_id));


            Message message = new Message();
            message.setId(CommonFunctions.getFieldInt(jsonObject, context.getString(R.string.id)));
            message.setMessage(CommonFunctions.getFieldString(jsonObject, context.getString(R.string.message_text)));
            message.setCreatedAt(CommonFunctions.getFieldString(jsonObject, context.getString(R.string.message_date)));
            message.setUserId(CommonFunctions.getFieldInt(jsonObject, context.getString(R.string.teacher_id)));
            message.setType(CommonFunctions.getFieldInt(jsonObject, context.getString(R.string.message_type)));
            message.setRead(CommonFunctions.getFieldInt(jsonObject, context.getString(R.string.message_read)));

            new MessageDBInterface(context).addMessage(jsonObject);

            Teacher teacher = new Teachers(context).findTeacherById(teacherId);

            if(teacher == null)
                return;


            if(Preferences.getBoolean(Preferences.NOTIFICATION_SETTING, false, getBaseContext())) {
                Intent notificationIntent = new Intent(context, ChatRoomActivity.class);
                notificationIntent.putExtra(Utils.TEACHER, teacher);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                buildNotification(text, text, teacher.getName(), contentIntent);
            }


            // broadcast for one chat
            Intent pushNotification = new Intent(ChatRoomActivity.CHAT_ROOM_RECEIVER);
            pushNotification.putExtra(Utils.MESSAGE_PUSH, message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


            //broadcasr for chats
            Intent chatRoomsNotification = new Intent(MessagesFragment.MESSAGES_RECEIVER);
            chatRoomsNotification.putExtra(Utils.MESSAGE_PUSH, message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(chatRoomsNotification);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}