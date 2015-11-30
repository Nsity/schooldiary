package com.example.nsity.schooldiary.navigation.timetable;


import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 16.11.15.
 */
public class Timetable {

    private ArrayList<TimetableItem> timetableArrayList = null;
    private Context context;

    public Timetable(Context context) {
        this.context = context;
        timetableArrayList = loadFromDB();
    }

    private ArrayList<TimetableItem> loadFromDB() {
        TimetableDBInterface db = new TimetableDBInterface(context);

        Cursor cursor = db.getTimetable();
        ArrayList<TimetableItem> timetable = new ArrayList<>();

        if(cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            do {
                TimetableItem timetableItem = new TimetableItem();
                timetableItem.setId(cursor.getInt(cursor.getColumnIndex(TimetableDBInterface.TIMETABLE_COLUMN_ID)));
                timetableItem.setRoom(cursor.getString(cursor.getColumnIndex(TimetableDBInterface.TIMETABLE_COLUMN_ROOM)));
                timetableItem.setSubjectId(cursor.getInt(cursor.getColumnIndex(TimetableDBInterface.TIMETABLE_COLUMN_SUBJECTS_CLASS_ID)));
                timetableItem.setSubject(cursor.getString(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_SUBJECT_NAME)));
                timetableItem.setColor(cursor.getInt(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_COLOR)));
                timetableItem.setTimeStart(cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_START)));
                timetableItem.setTimeEnd(cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_END)));
                timetableItem.setTimeId(cursor.getInt(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_ID)));
                timetableItem.setDay(cursor.getInt(cursor.getColumnIndex(TimetableDBInterface.TIMETABLE_COLUMN_DAY_OF_WEEK)));

                timetable.add(timetableItem);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.closeDB();

        if(timetable.size() == 0) {
            return null;
        } else {
            return timetable;
        }
    }

    public ArrayList<TimetableItem> getTimetableOfDay(int dayOfWeek) {
        ArrayList<TimetableItem> timetableOfDay = new ArrayList<>();

        for (TimetableItem timetableItem: timetableArrayList) {
            if(timetableItem.getDay() == dayOfWeek) {
                timetableOfDay.add(timetableItem);
            }
        }

        if(timetableOfDay.size() == 0) {
            return null;
        } else
            return timetableOfDay;
    }
}
