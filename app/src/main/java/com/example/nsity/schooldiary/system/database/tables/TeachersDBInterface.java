package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Teacher;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 17.04.16.
 */
public class TeachersDBInterface extends ADBWorker {

    public static final String TEACHER_TABLE_NAME = "TEACHER";
    public static final String TEACHER_COLUMN_ID = "TEACHER_ID";
    public static final String TEACHER_COLUMN_NAME = "TEACHER_NAME";


    public static final String TEACHER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TEACHER_TABLE_NAME + "("
            + TEACHER_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + TEACHER_COLUMN_NAME + " VARCHAR(5000) " + ");";


    public TeachersDBInterface(Context context) {
        super(context);
    }

    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if (dropAllData) {
            delete(TEACHER_TABLE_NAME, null, null);
        }

        return addTeachers(objects);
    }

    private int addTeachers(JSONArray periods) {
        if ((CommonFunctions.StringIsNullOrEmpty(periods.toString())) || (periods.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> periodsValues = new ArrayList<>();
            for (int i = 0; i < periods.length(); i++) {
                JSONObject period = periods.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(TEACHER_COLUMN_ID, CommonFunctions.getFieldInt(period, context.getString(R.string.id)));
                cv.put(TEACHER_COLUMN_NAME, CommonFunctions.getFieldString(period, context.getString(R.string.name_short)));
                periodsValues.add(cv);
            }
            return insert(TEACHER_TABLE_NAME, ADBWorker.REPLACE, periodsValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public Teacher getTeacherById(int id) {
        String selectQuery = "SELECT * FROM " + TEACHER_TABLE_NAME + " WHERE " + TEACHER_COLUMN_ID + " =?";

        Cursor cursor = getCursor(selectQuery, new String[]{String.valueOf(id)});

        if(cursor == null) {
            return null;
        }

        Teacher teacher = null;

        if (cursor.moveToFirst()) {
            do {
                teacher = new Teacher(cursor.getInt(cursor.getColumnIndex(TEACHER_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(TEACHER_COLUMN_NAME)));
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return teacher;
    }


    /*public ArrayList<Teacher> getTeachers() {
        String selectQuery = "SELECT * FROM " + TEACHER_TABLE_NAME;

        Cursor cursor = getCursor(selectQuery, null);

        if(cursor == null) {
            return null;
        }

        Teacher teacher = null;

        if (cursor.moveToFirst()) {
            do {
                teacher = new Teacher(cursor.getInt(cursor.getColumnIndex(TEACHER_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(TEACHER_COLUMN_NAME)));
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return null;
    }*/
}
