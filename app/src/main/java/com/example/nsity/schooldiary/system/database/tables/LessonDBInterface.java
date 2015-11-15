package com.example.nsity.schooldiary.system.database.tables;

import android.content.Context;

import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;

/**
 * Created by nsity on 15.11.15.
 */
public class LessonDBInterface extends ADBWorker {

    public static final String LESSON_TABLE_NAME = "LESSON";
    public static final String LESSON_COLUMN_ID = "LESSON_ID";
    public static final String LESSON_COLUMN_THEME = "LESSON_THEME";
    public static final String LESSON_COLUMN_SUBJECTS_CLASS_ID = "SUBJECTS_CLASS_ID";
    public static final String LESSON_COLUMN_HOMEWORK = "LESSON_HOMEWORK";
    public static final String LESSON_COLUMN_STATUS = "LESSON_STATUS";
    public static final String LESSON_COLUMN_TIME_ID = "TIME_ID";
    public static final String LESSON_COLUMN_DATE = "LESSON_DATE";


    public static final String LESSON_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + LESSON_TABLE_NAME + "("
            + LESSON_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + LESSON_COLUMN_HOMEWORK + " VARCHAR(5000), "
            + LESSON_COLUMN_THEME + " VARCHAR(300), "
            + LESSON_COLUMN_SUBJECTS_CLASS_ID + " INTEGER, "
            + LESSON_COLUMN_DATE + " DATE, "
            + LESSON_COLUMN_STATUS + " BOOLEAN, "
            + LESSON_COLUMN_TIME_ID + " INTEGER " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        return 0;
    }

    public LessonDBInterface(Context context) {
        super(context);
    }
}
