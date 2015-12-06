package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Period;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 06.12.15.
 */
public class PeriodDBInterface extends ADBWorker {

    public static final String PERIOD_TABLE_NAME = "PERIOD";
    public static final String PERIOD_COLUMN_ID = "PERIOD_ID";
    public static final String PERIOD_COLUMN_START = "PERIOD_START";
    public static final String PERIOD_COLUMN_END = "PERIOD_END";
    public static final String PERIOD_COLUMN_NAME = "PERIOD_NAME";

    public static final String PERIOD_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + PERIOD_TABLE_NAME + "("
            + PERIOD_COLUMN_ID + " INTEGER PRIMARY KEY , "
            + PERIOD_COLUMN_START + " DATE, "
            + PERIOD_COLUMN_END + " DATE, "
            + PERIOD_COLUMN_NAME + " VARCHAR(255) "
            + ");";

    public PeriodDBInterface(Context context) {
        super(context);
    }




    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if (dropAllData) {
            delete(PERIOD_TABLE_NAME, null, null);
        }

        return addPeriods(objects);
    }

    private int addPeriods(JSONArray periods) {
        if ((CommonFunctions.StringIsNullOrEmpty(periods.toString())) || (periods.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> periodsValues = new ArrayList<>();
            for (int i = 0; i < periods.length(); i++) {
                JSONObject period = periods.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(PERIOD_COLUMN_ID, CommonFunctions.getFieldInt(period, context.getString(R.string.id)));
                cv.put(PERIOD_COLUMN_START, CommonFunctions.getFieldString(period, context.getString(R.string.time_start)));
                cv.put(PERIOD_COLUMN_END, CommonFunctions.getFieldString(period, context.getString(R.string.time_end)));
                cv.put(PERIOD_COLUMN_NAME, CommonFunctions.getFieldString(period, context.getString(R.string.name)));
                periodsValues.add(cv);
            }
            return insert(PERIOD_TABLE_NAME, ADBWorker.REPLACE, periodsValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<Period> getPeriods() {
        String selectQuery = "SELECT * FROM " + PERIOD_TABLE_NAME + " ORDER BY " + PERIOD_COLUMN_NAME;

        Cursor cursor = getCursor(selectQuery, new String[]{});

        if(cursor == null)
            return null;

        ArrayList<Period> periods = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Period period = new Period(cursor.getInt(cursor.getColumnIndex(PERIOD_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(PERIOD_COLUMN_START)),
                        cursor.getString(cursor.getColumnIndex(PERIOD_COLUMN_END)),
                        cursor.getString(cursor.getColumnIndex(PERIOD_COLUMN_NAME)));
                periods.add(period);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        if(periods.size() == 0) {
            return null;
        } else {
            return periods;
        }
    }
}
