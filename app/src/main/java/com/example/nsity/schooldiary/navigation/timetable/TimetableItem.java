package com.example.nsity.schooldiary.navigation.timetable;

import com.example.nsity.schooldiary.navigation.marks.Subject;
import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 29.11.15.
 */
public class TimetableItem extends BaseEntity {

    private String room;
    private int day;
    private Subject subject;
    private Time time;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
