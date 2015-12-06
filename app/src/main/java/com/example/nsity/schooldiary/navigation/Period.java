package com.example.nsity.schooldiary.navigation;

import java.io.Serializable;

/**
 * Created by nsity on 06.12.15.
 */
public class Period implements Serializable {

    private int id;
    private String periodStart;
    private String periodEnd;
    private String name;

    public Period(int id, String periodStart, String periodEnd, String name) {
        this.id = id;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
