package com.example.nsity.schooldiary.navigation.timetable;


import android.content.Context;

import com.example.nsity.schooldiary.system.ListBaseEntity;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 16.11.15.
 */
public class Timetable extends ListBaseEntity {

    private ArrayList<TimetableItem> timetableArrayList;
    private TimetableDBInterface db;

    public Timetable(Context context) {
        super(context);
        this.db = new TimetableDBInterface(context);
        this.timetableArrayList = new ArrayList<>();
        loadFromDB();
    }

    @Override
    protected void loadFromDB() {
        timetableArrayList.clear();
        timetableArrayList = db.getTimetable();
    }

    public ArrayList<TimetableItem> getTimetableOfDay(int dayOfWeek) {
        ArrayList<TimetableItem> timetableOfDay = new ArrayList<>();

        for (TimetableItem timetableItem: timetableArrayList) {
            if(timetableItem.getDay() == dayOfWeek) {
                timetableOfDay.add(timetableItem);
            }
        }

        return timetableOfDay.size() == 0 ? null : timetableOfDay;
    }

    public ArrayList<TimetableItem> getTimetable() {
        return timetableArrayList;
    }

    public TimetableItem getItem(int position) {
        return timetableArrayList.get(position);
    }
}
