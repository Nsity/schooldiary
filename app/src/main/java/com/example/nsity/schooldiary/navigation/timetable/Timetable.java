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

    private ArrayList<TimetableItem> timetableArrayList;

    TimetableDBInterface db;

    public Timetable(Context context) {
        this.db = new TimetableDBInterface(context);
        timetableArrayList = loadFromDB();
    }

    private ArrayList<TimetableItem> loadFromDB() {
        return db.getTimetable();
    }

    public ArrayList<TimetableItem> getTimetableOfDay(int dayOfWeek) {
        if(timetableArrayList == null) {
            return null;
        }

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


    public TimetableItem getItem(int position) {
        return timetableArrayList.get(position);
    }
}
