package com.example.nsity.schooldiary.navigation.timetable;

import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.navigation.Time;

import java.io.Serializable;

/**
 * Created by nsity on 29.11.15.
 */
public class TimetableItem implements Serializable {

    private int id;
    private String room;
    private int day;
    private Subject subject;
    private Time time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
