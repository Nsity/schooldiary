package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.navigation.Time;
import com.example.nsity.schooldiary.navigation.timetable.TimetableItem;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
    public int save(JSONArray clients, boolean dropAllData) {
        if (dropAllData) {
            delete(TIMETABLE_TABLE_NAME, null, null);
        }

        return addTimetableData(clients);
    }


    public int addTimetableData(JSONArray timetable) {
        if ((CommonFunctions.StringIsNullOrEmpty(timetable.toString())) || (timetable.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> subjectsValues = new ArrayList<>();
            for (int i = 0; i < timetable.length(); i++) {
                JSONArray subjects = timetable.getJSONArray(i);
                for (int j = 0; j < subjects.length(); j++) {
                    JSONObject subject = subjects.getJSONObject(j);
                    ContentValues cv = new ContentValues();
                    cv.put(TIMETABLE_COLUMN_ID, CommonFunctions.getFieldInt(subject, context.getString(R.string.id)));
                    cv.put(TIMETABLE_COLUMN_ROOM, CommonFunctions.getFieldString(subject, context.getString(R.string.room_name)));
                    cv.put(TIMETABLE_COLUMN_SUBJECTS_CLASS_ID, CommonFunctions.getFieldInt(subject, context.getString(R.string.subject_id)));
                    cv.put(TIMETABLE_COLUMN_TIME_ID, CommonFunctions.getFieldInt(subject, context.getString(R.string.time_id)));
                    cv.put(TIMETABLE_COLUMN_DAY_OF_WEEK, CommonFunctions.getFieldInt(subject, context.getString(R.string.day_id)));

                    subjectsValues.add(cv);
                }
            }
            return insert(TIMETABLE_TABLE_NAME, ADBWorker.REPLACE, subjectsValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<TimetableItem> getTimetable() {
        String selectQuery = "SELECT * FROM " + TIMETABLE_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + TIMETABLE_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + TIMETABLE_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " ORDER BY " + TIMETABLE_COLUMN_DAY_OF_WEEK + ", " +  TimeDBInterface.TIME_COLUMN_START;


        Cursor cursor = getCursor(selectQuery, new String[]{});
        if(cursor == null) {
            return null;
        }

        ArrayList<TimetableItem> timetable = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                TimetableItem timetableItem = new TimetableItem();
                timetableItem.setId(cursor.getInt(cursor.getColumnIndex(TIMETABLE_COLUMN_ID)));
                timetableItem.setRoom(cursor.getString(cursor.getColumnIndex(TIMETABLE_COLUMN_ROOM)));
                timetableItem.setDay(cursor.getInt(cursor.getColumnIndex(TIMETABLE_COLUMN_DAY_OF_WEEK)));

                timetableItem.setSubject(new Subject(cursor.getInt(cursor.getColumnIndex(TIMETABLE_COLUMN_SUBJECTS_CLASS_ID)),
                        cursor.getString(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_SUBJECT_NAME)),
                        cursor.getInt(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_COLOR))));


                timetableItem.setTime(new Time(cursor.getInt(cursor.getColumnIndex(TIMETABLE_COLUMN_TIME_ID)),
                        cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_START)),
                        cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_END))));

                timetable.add(timetableItem);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        if(timetable.size() == 0) {
            return null;
        } else {
            return timetable;
        }
    }

    public TimetableDBInterface(Context context) {
        super(context);
    }
}
