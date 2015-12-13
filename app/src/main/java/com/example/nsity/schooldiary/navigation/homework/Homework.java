package com.example.nsity.schooldiary.navigation.homework;

import com.example.nsity.schooldiary.navigation.Subject;

/**
 * Created by nsity on 13.12.15.
 */
public class Homework {

    private Subject subject;
    private String task;
    private String date;
    private String theme;

    public Homework() {

    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
