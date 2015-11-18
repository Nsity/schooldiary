package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
    public int save(JSONArray clients, boolean dropAllData) {
        if (dropAllData) {
            delete(TIME_TABLE_NAME, null, null);
        }

        return addTimesData(clients);
    }


    public int addTimesData(JSONArray times) {
        if ((CommonFunctions.StringIsNullOrEmpty(times.toString())) || (times.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> timesValues = new ArrayList<>();
            for (int i = 0; i < times.length(); i++) {
                JSONObject time = times.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(TIME_COLUMN_ID, CommonFunctions.getFieldInt(time, context.getString(R.string.id)));
                cv.put(TIME_COLUMN_START, CommonFunctions.getFieldString(time, context.getString(R.string.time_start)));
                cv.put(TIME_COLUMN_END, CommonFunctions.getFieldString(time, context.getString(R.string.time_end)));

                timesValues.add(cv);
            }
            int result = insert(TIME_TABLE_NAME, ADBWorker.REPLACE, timesValues);
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public TimeDBInterface(Context context) {
        super(context);
    }
}
