package com.example.nsity.schooldiary.system.database.tables;

import android.content.Context;

import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;

/**
 * Created by nsity on 15.11.15.
 */
public class TimetableDBInterface extends ADBWorker {

    public static final String TIMETABLE_TABLE_NAME = "TIMETABLE";
    public static final String TIMETABLE_COLUMN_ID = "TIMETABLE_ID";
    public static final String TIMETABLE_COLUMN_ROOM = "TIMETABLE_ROOM";
    public static final String TIMETABLE_COLUMN_DAY_OF_WEEK = "TIMETABLE_DAYOFWEEK";
    public static final String TIMETABLE_COLUMN_SUBJECTS_CLASS_ID = "SUBJECTS_CLASS_ID";
    public static final String TIMETABLE_COLUMN_TIME_ID = "TIME_ID";


    public static final String TIMETABLE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TIMETABLE_TABLE_NAME + "("
            + TIMETABLE_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + TIMETABLE_COLUMN_ROOM + " VARCHAR(20), "
            + TIMETABLE_COLUMN_DAY_OF_WEEK + " INTEGER, "
            + TIMETABLE_COLUMN_TIME_ID + " INTEGER, "
            + TIMETABLE_COLUMN_SUBJECTS_CLASS_ID + " INTEGER " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        return 0;
    }

    public TimetableDBInterface(Context context) {
        super(context);
    }
}
