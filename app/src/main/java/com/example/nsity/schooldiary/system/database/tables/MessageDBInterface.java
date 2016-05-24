package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.messages.ChatRoom;
import com.example.nsity.schooldiary.navigation.messages.Message;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.Preferences;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 27.04.16.
 */
public class MessageDBInterface extends ADBWorker {



    public static final int PUPIL_TYPE = 2;
    public static final int TEACHER_TYPE = 1;
    //type 1 - teacher
    //type 2 - pupil

    public static final String MESSAGE_TABLE_NAME = "MESSAGE";
    public static final String MESSAGE_COLUMN_ID = "PUPIL_MESSAGE_ID";
    public static final String MESSAGE_COLUMN_TEACHER_ID = "TEACHER_ID";
    public static final String MESSAGE_COLUMN_TEXT = "MESSAGE_TEXT";
    public static final String MESSAGE_COLUMN_CREATED_AT = "MESSAGE_CREATED_AT";
    public static final String MESSAGE_COLUMN_TYPE = "MESSAGE_TYPE";
    public static final String MESSAGE_COLUMN_READ = "MESSAGE_READ";


    public static final String MESSAGE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + MESSAGE_TABLE_NAME + "("
            + MESSAGE_COLUMN_ID  + " INTEGER PRIMARY KEY , "
            + MESSAGE_COLUMN_TEACHER_ID + " INTEGER, "
            + MESSAGE_COLUMN_TEXT + " TEXT, "
            + MESSAGE_COLUMN_TYPE + " INTEGER, "
            + MESSAGE_COLUMN_READ + " INTEGER, "
            + MESSAGE_COLUMN_CREATED_AT + " DATETIME "
            + ");";


    public MessageDBInterface(Context context) {
        super(context);
    }

    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if(dropAllData) {
            delete(MESSAGE_TABLE_NAME, null, null);
        }
        return addMessages(objects);
    }


    public void deleteConversation(int teacherId) {
        delete(MESSAGE_TABLE_NAME, MESSAGE_COLUMN_TEACHER_ID + " =?", new String[] {String.valueOf(teacherId)});
    }


    public int addMessages(JSONArray messages) {
        if ((CommonFunctions.StringIsNullOrEmpty(messages.toString())) || (messages.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> messagesValues = new ArrayList<>();
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                messagesValues.add(saveMessage(message));
            }
            return insert(MESSAGE_TABLE_NAME, ADBWorker.REPLACE, messagesValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public long addMessage(JSONObject message) {
        return insert(MESSAGE_TABLE_NAME, saveMessage(message));
    }


    private ContentValues saveMessage(JSONObject message) {
        ContentValues cv = new ContentValues();
        cv.put(MESSAGE_COLUMN_ID, CommonFunctions.getFieldInt(message, context.getString(R.string.id)));
        cv.put(MESSAGE_COLUMN_TEXT, CommonFunctions.getFieldString(message, context.getString(R.string.message_text)));
        cv.put(MESSAGE_COLUMN_CREATED_AT, CommonFunctions.getFieldString(message, context.getString(R.string.message_date)));
        cv.put(MESSAGE_COLUMN_TEACHER_ID, CommonFunctions.getFieldInt(message, context.getString(R.string.teacher_id)));
        cv.put(MESSAGE_COLUMN_TYPE, CommonFunctions.getFieldInt(message, context.getString(R.string.message_type)));
        cv.put(MESSAGE_COLUMN_READ, CommonFunctions.getFieldInt(message, context.getString(R.string.message_read)));

        return cv;
    }


    public ArrayList<ChatRoom> getChatRooms() {
        ArrayList<ChatRoom> arrayList = new ArrayList<>();

        String selectQuery = "SELECT " + MESSAGE_COLUMN_TEACHER_ID + " FROM " + MESSAGE_TABLE_NAME + " GROUP BY " + MESSAGE_COLUMN_TEACHER_ID;

        Cursor cursor = getCursor(selectQuery, new String[]{});

        if(cursor == null) {
            return arrayList;
        }


        if (cursor.moveToFirst()) {
            do {
                int teacherId = cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_TEACHER_ID));
                selectQuery = "SELECT * FROM " + MESSAGE_TABLE_NAME + " WHERE " + MESSAGE_COLUMN_TEACHER_ID + " = ? " +
                        "ORDER BY " + MESSAGE_COLUMN_CREATED_AT + " DESC LIMIT 1";

                Cursor cursor1 = getCursor(selectQuery, new String[]{String.valueOf(teacherId)});

                if(cursor1 == null) {
                    return arrayList;
                }
                if (cursor1.moveToFirst()) {
                    do {
                        ChatRoom chatRoom = new ChatRoom(cursor1.getInt(cursor1.getColumnIndex(MESSAGE_COLUMN_ID)),
                                teacherId, cursor1.getString(cursor1.getColumnIndex(MESSAGE_COLUMN_TEXT)),
                                cursor1.getString(cursor1.getColumnIndex(MESSAGE_COLUMN_CREATED_AT)),
                                getNewMessages(teacherId));
                        arrayList.add(chatRoom);
                    } while (cursor1.moveToNext());
                }

                cursor1.close();
            } while (cursor.moveToNext());
        }

        cursor.close();

        return arrayList;
    }

    public int getNewMessages(int teacherId) {
        String selectQuery = "SELECT * AS COUNT FROM " + MESSAGE_TABLE_NAME +
                " WHERE " + MESSAGE_COLUMN_TEACHER_ID + " = ? AND " + MESSAGE_COLUMN_READ + " = 0 AND " + MESSAGE_COLUMN_TYPE + " = 1";

        Cursor cursor = getCursor(selectQuery, new String[]{ String.valueOf(teacherId) });

        if(cursor == null) {
            return 0;
        }

        return cursor.getCount();
    }



    public ArrayList<Message> getMessagesInConversation(int teacherId) {
        ArrayList<Message> arrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + MESSAGE_TABLE_NAME + " WHERE " + MESSAGE_COLUMN_TEACHER_ID + " =? "
                + " GROUP BY " + MESSAGE_COLUMN_CREATED_AT;

        Cursor cursor = getCursor(selectQuery, new String[]{ String.valueOf(teacherId) });

        if(cursor == null) {
            return arrayList;
        }

        if (cursor.moveToFirst()) {
            do {
                Message message = new Message(cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_TEXT)),
                        cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_CREATED_AT)),
                        cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_TYPE)));
                arrayList.add(message);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return arrayList;
    }
}
