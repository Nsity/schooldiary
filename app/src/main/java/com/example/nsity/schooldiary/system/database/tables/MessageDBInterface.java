package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.messages.Message;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 27.04.16.
 */
public class MessageDBInterface extends ADBWorker {

    public static final String MESSAGE_TABLE_NAME = "MESSAGE";
    public static final String MESSAGE_COLUMN_ID = "MESSAGE_ID";
    public static final String MESSAGE_COLUMN_TEACHER_ID = "TEACHER_ID";
    public static final String MESSAGE_COLUMN_TEXT = "MESSAGE_TEXT";
    public static final String MESSAGE_COLUMN_CREATED_AT = "MESSAGE_CREATED_AT";


    public static final String MESSAGE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + MESSAGE_TABLE_NAME + "("
            + MESSAGE_COLUMN_ID  + " INTEGER PRIMARY KEY , "
            + MESSAGE_COLUMN_TEACHER_ID + " INTEGER, "
            + MESSAGE_COLUMN_TEXT + " TEXT, "
            + MESSAGE_COLUMN_CREATED_AT + " DATETIME, "
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



    public int addMessages(JSONArray messages) {
        if ((CommonFunctions.StringIsNullOrEmpty(messages.toString())) || (messages.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> messagesValues = new ArrayList<>();
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(MESSAGE_COLUMN_ID, CommonFunctions.getFieldInt(message, context.getString(R.string.id)));
                cv.put(MESSAGE_COLUMN_TEXT, CommonFunctions.getFieldString(message, context.getString(R.string.message_text)));
                cv.put(MESSAGE_COLUMN_CREATED_AT, CommonFunctions.getFieldString(message, context.getString(R.string.send_date)));
                cv.put(MESSAGE_COLUMN_TEACHER_ID, CommonFunctions.getFieldInt(message, context.getString(R.string.teacher_id)));

                messagesValues.add(cv);
            }
            return insert(MESSAGE_TABLE_NAME, ADBWorker.REPLACE, messagesValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long addMessage(Message message, int teacherId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_COLUMN_ID, message.getId());
        contentValues.put(MESSAGE_COLUMN_TEXT, message.getMessage());
        contentValues.put(MESSAGE_COLUMN_CREATED_AT, message.getCreatedAt());
        contentValues.put(MESSAGE_COLUMN_TEACHER_ID, teacherId);

        return insert(MESSAGE_TABLE_NAME, contentValues);
    }
}
