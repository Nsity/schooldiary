package com.example.nsity.schooldiary.navigation;

import java.io.Serializable;

/**
 * Created by nsity on 06.12.15.
 */
public class Time implements Serializable {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
