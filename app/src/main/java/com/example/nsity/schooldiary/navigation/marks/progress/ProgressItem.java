package com.example.nsity.schooldiary.navigation.marks.progress;

import com.example.nsity.schooldiary.navigation.Period;
import com.example.nsity.schooldiary.navigation.Subject;

import java.io.Serializable;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressItem implements Serializable {

    private int id;
    private int value;
    private Subject subject;
    private Period period;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }


}
