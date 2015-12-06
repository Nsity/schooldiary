package com.example.nsity.schooldiary.navigation.lesson;

import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.navigation.Time;
import com.example.nsity.schooldiary.navigation.marks.Mark;
import com.example.nsity.schooldiary.system.database.tables.LessonDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 24.11.15.
 */
public class Lesson {

    private int id;
    private String date;
    private String theme;
    private String homework;
    private String note;
    private String pass;

    private Time time;
    private Subject subject;

    private ArrayList<Mark> marksArrayList;

    private LessonDBInterface db;

    private int color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Lesson(Context context, int lessonId) {
        this.id = lessonId;
        this.db = new LessonDBInterface(context);
        //loadFromDB();
    }

    public Lesson(Context context) {
        this.db = new LessonDBInterface(context);
    }

    public Lesson() {
    }

    public Lesson(Context context, String date, int timeId, int subjectId) {
        this.db = new LessonDBInterface(context);
        getLesson(date, timeId, subjectId);
    }

    /*private void loadFromDB() {
        Cursor cursor = db.getLesson(String.valueOf(id));

        if(cursor == null) {
            return;
        }

        if (cursor.moveToFirst()) {
            do {
                setValues(cursor);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.closeDB();

        marksArrayList = getMarksOfLesson();
    }*/

    public void getLesson(String date, int timeId, int subjectId) {
        Lesson lesson = db.getLesson(date, timeId, subjectId);

        if(lesson == null) {
            setId(-1);
            return;
        }

        this.id = lesson.getId();
        this.date = lesson.getDate();
        this.homework = lesson.getHomework();
        this.time = lesson.getTime();
        this.pass = lesson.getPass();
        this.theme = lesson.getTheme();
        this.subject = lesson.getSubject();
        this.note = lesson.getNote();

        getMarksOfLesson();
    }

    public void getMarksOfLesson() {
        marksArrayList = db.getMarks(id);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public ArrayList<Mark> getMarks() {
        return marksArrayList;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}
