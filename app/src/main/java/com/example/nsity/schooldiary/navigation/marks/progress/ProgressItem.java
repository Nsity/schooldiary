package com.example.nsity.schooldiary.navigation.marks.progress;

import com.example.nsity.schooldiary.navigation.timetable.Period;
import com.example.nsity.schooldiary.system.BaseEntity;

/**
 * Created by nsity on 06.12.15.
 */
public class ProgressItem extends BaseEntity {

    private int value;
    private Period period;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

}
