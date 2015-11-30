package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 18.11.15.
 */
public class MarkDBInterface extends ADBWorker {

    public static final String MARK_TABLE_NAME = "MARK";
    public static final String MARK_COLUMN_ID = "MARK_ID";
    public static final String MARK_COLUMN_VALUE = "MARK_VALUE";
    public static final String MARK_COLUMN_LESSON_ID = "LESSON_ID";
    public static final String MARK_COLUMN_TYPE = "MARK_TYPE";


    public static final String MARK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + MARK_TABLE_NAME + "("
            + MARK_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + MARK_COLUMN_VALUE + " INTEGER, "
            + MARK_COLUMN_TYPE + " VARCHAR(500), "
            + MARK_COLUMN_LESSON_ID + " INTEGER " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if (dropAllData) {
            delete(MARK_TABLE_NAME, null, null);
        }
        return addMarks(objects);
    }

    private int addMarks(JSONArray marks) {
        if ((CommonFunctions.StringIsNullOrEmpty(marks.toString())) || (marks.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> marksValues = new ArrayList<>();
            for (int i = 0; i < marks.length(); i++) {
                JSONObject lesson = marks.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(MARK_COLUMN_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.id)));
                cv.put(MARK_COLUMN_VALUE, CommonFunctions.getFieldInt(lesson, context.getString(R.string.mark)));
                cv.put(MARK_COLUMN_LESSON_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.lessonId)));
                cv.put(MARK_COLUMN_TYPE, CommonFunctions.getFieldString(lesson, context.getString(R.string.type)));

                marksValues.add(cv);
            }
            return insert(MARK_TABLE_NAME, ADBWorker.REPLACE, marksValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public MarkDBInterface(Context context) {
        super(context);
    }


    public Cursor getMarks(int lessonId) {
        String selectQuery = "SELECT * FROM " + MARK_TABLE_NAME + " WHERE " + MARK_COLUMN_LESSON_ID + " =?";
        return getCursor(selectQuery, new String[]{String.valueOf(lessonId)});
    }


}
