package com.example.nsity.schooldiary.system.database.tables;

import android.content.Context;

import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;

/**
 * Created by nsity on 18.11.15.
 */
public class MarkDBInterface extends ADBWorker {

    public static final String MARK_TABLE_NAME = "MARK";
    public static final String MARK_COLUMN_ID = "MARK_ID";
    public static final String MARK_COLUMN_VALUE = "MARK_VALUE";
    public static final String MARK_COLUMN_LESSON_ID = "LESSON_ID";


    public static final String MARK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + MARK_TABLE_NAME + "("
            + MARK_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + MARK_COLUMN_VALUE + " INTEGER, "
            + MARK_COLUMN_LESSON_ID + " INTEGER " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        return 0;
    }

    public MarkDBInterface(Context context) {
        super(context);
    }

}
