package com.example.nsity.schooldiary.navigation.homework;

/**
 * Created by fedorova on 25.11.2015.
 */
public class Homework {
    private int id;
    private String subjectName;
    private String task;
    private int lessonId;
    private String date;

    public Homework() {

    }

    public Homework(int id, String task, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
}