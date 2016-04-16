package com.example.nsity.schooldiary.navigation.lesson;

import android.content.Context;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.marks.Subject;
import com.example.nsity.schooldiary.navigation.timetable.Time;
import com.example.nsity.schooldiary.navigation.marks.Mark;
import com.example.nsity.schooldiary.system.BaseEntity;
import com.example.nsity.schooldiary.system.CommonManager;
import com.example.nsity.schooldiary.system.database.tables.LessonsDBInterface;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;

import java.util.ArrayList;

/**
 * Created by nsity on 24.11.15.
 */
public class Lesson extends BaseEntity {

    private String date;
    private String theme;
    private String homework;
    private String note;
    private String pass;
    private Time time;
    private Subject subject;
    private boolean isHomeworkCompleted;

    private ArrayList<Mark> marksArrayList;

    private LessonsDBInterface db;

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

    public Lesson() {
    }

    public Lesson(Context context, String date, int timeId, int subjectId) {
        this.db = new LessonsDBInterface(context);
        loadFromDB(date, timeId, subjectId);
    }

    public void loadFromDB(String date, int timeId, int subjectId) {
        Lesson lesson = db.getLesson(date, timeId, subjectId);

        if(lesson.getId() == -1) {
            this.id = -1;
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

        marksArrayList = loadMarks();
    }

    public void load(Context context, final int subjectId, final String day,
                     final int timeId, final CallBack callBack) {
        if (!Server.isOnline(context)) {
            callBack.onFail(context.getString(R.string.internet_problem));
            return;
        }

        CommonManager.getLesson(context, id, subjectId, day, timeId, new CallBack() {
            @Override
            public void onSuccess() {
                loadFromDB(day, timeId, subjectId);
                callBack.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callBack.onFail(error);
            }
        });
    }

    public ArrayList<Mark> loadMarks() {
        return db.getMarks(id);
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

    public boolean isHomeworkCompleted() {
        return isHomeworkCompleted;
    }

    public void setIsHomeworkCompleted(boolean isHomeworkCompleted) {
        this.isHomeworkCompleted = isHomeworkCompleted;
    }
}
