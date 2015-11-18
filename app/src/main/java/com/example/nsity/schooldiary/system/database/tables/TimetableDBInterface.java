package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.timetable.Timetable;
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
                    cv.put(TIMETABLE_COLUMN_ROOM, CommonFunctions.getFieldString(subject, context.getString(R.string.room_name)).equals("null") ? "" :
                            CommonFunctions.getFieldString(subject, context.getString(R.string.room_name)));
                    cv.put(TIMETABLE_COLUMN_SUBJECTS_CLASS_ID, CommonFunctions.getFieldInt(subject, context.getString(R.string.subject_id)));
                    cv.put(TIMETABLE_COLUMN_TIME_ID, CommonFunctions.getFieldInt(subject, context.getString(R.string.time_id)));
                    cv.put(TIMETABLE_COLUMN_DAY_OF_WEEK, i + 1);

                    subjectsValues.add(cv);
                }
            }
            int result = insert(TIMETABLE_TABLE_NAME, ADBWorker.REPLACE, subjectsValues);
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<Timetable> getTimetable(int dayOfWeek) {
        String selectQuery = "SELECT * FROM " + TIMETABLE_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + TIMETABLE_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + TIMETABLE_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " WHERE " + TIMETABLE_COLUMN_DAY_OF_WEEK + " =? ORDER BY " + TimeDBInterface.TIME_COLUMN_START;
        Cursor cursor = getCursor(selectQuery, new String[]{String.valueOf(dayOfWeek)});
        ArrayList<Timetable> arrayList = new ArrayList<>();

        if(cursor.getCount() == 0) {
            return arrayList;
        }

        if (cursor.moveToFirst()) {
            do {
                Timetable timetable = new Timetable();
                timetable.setId(cursor.getInt(cursor.getColumnIndex(TIMETABLE_COLUMN_ID)));
                timetable.setRoom(cursor.getString(cursor.getColumnIndex(TIMETABLE_COLUMN_ROOM)));
                timetable.setSubjectId(cursor.getString(cursor.getColumnIndex(TIMETABLE_COLUMN_SUBJECTS_CLASS_ID)));
                timetable.setSubject(cursor.getString(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_SUBJECT_NAME)));
                timetable.setColor(cursor.getInt(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_COLOR)));
                timetable.setTimeStart(cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_START)));
                timetable.setTimeEnd(cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_END)));
                timetable.setTimeId(cursor.getInt(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_ID)));
                arrayList.add(timetable);
            }
            while (cursor.moveToNext());
        } else
            return arrayList;

        cursor.close();

        return arrayList;
    }


    public TimetableDBInterface(Context context) {
        super(context);
    }
}
