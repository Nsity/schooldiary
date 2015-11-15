package com.example.nsity.schooldiary.system.database.tables;

import android.content.Context;

import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;

/**
 * Created by nsity on 15.11.15.
 */
public class TimeDBInterface extends ADBWorker {


    public static final String TIME_TABLE_NAME = "TIME";
    public static final String TIME_COLUMN_ID = "TIME_ID";
    public static final String TIME_COLUMN_START = "TIME_START";
    public static final String TIME_COLUMN_END = "TIME_END";


    public static final String TIME_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TIME_TABLE_NAME + "("
            + TIME_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + TIME_COLUMN_START + " TIME, "
            + TIME_COLUMN_END + " TIME " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        return 0;
    }

    public TimeDBInterface(Context context) {
        super(context);
    }
}
