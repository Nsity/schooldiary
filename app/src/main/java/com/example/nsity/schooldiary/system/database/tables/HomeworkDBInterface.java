package com.example.nsity.schooldiary.system.database.tables;

import android.content.Context;

import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;

/**
 * Created by nsity on 02.01.16.
 */
public class HomeworkDBInterface extends ADBWorker {

    public static final String HOMEWORK_TABLE_NAME = "HOMEWORK";
    public static final String HOMEWORK_COLUMN_ID = "HOMEWORK_ID";
    public static final String HOMEWORK_COLUMN_SUBJECTS_CLASS_ID = "SUBJECTS_CLASS_ID";
    public static final String HOMEWORK_COLUMN_STATUS = "HOMEWORK_STATUS";
    public static final String HOMEWORK_COLUMN_TIME_ID = "TIME_ID";
    public static final String HOMEWORK_COLUMN_DATE = "HOMEWORK_DATE";
    public static final String HOMEWORK_COLUMN_TEXT = "HOMEWORK_TEXT";

    public static final String HOMEWORK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + HOMEWORK_TABLE_NAME + "("
            + HOMEWORK_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + HOMEWORK_COLUMN_TEXT + " text, "
            + HOMEWORK_COLUMN_SUBJECTS_CLASS_ID + " INTEGER, "
            + HOMEWORK_COLUMN_DATE + " DATE, "
            + HOMEWORK_COLUMN_STATUS + " BOOLEAN, "
            + HOMEWORK_COLUMN_TIME_ID + " INTEGER " + ");";

    public HomeworkDBInterface(Context context) {
        super(context);
    }

    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        return 0;
    }
}
