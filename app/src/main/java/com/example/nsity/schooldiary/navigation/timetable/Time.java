package com.example.nsity.schooldiary.navigation.timetable;

import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 06.12.15.
 */
public class Time extends BaseEntity {

    private String timeStart;
    private String timeEnd;

    public Time(int id, String timeStart, String timeEnd) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
