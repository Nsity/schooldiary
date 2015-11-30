package com.example.nsity.schooldiary.navigation.lesson;

import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.navigation.MarksFragment;
import com.example.nsity.schooldiary.navigation.marks.Mark;
import com.example.nsity.schooldiary.navigation.timetable.Timetable;
import com.example.nsity.schooldiary.system.database.tables.LessonDBInterface;
import com.example.nsity.schooldiary.system.database.tables.MarkDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 24.11.15.
 */
public class Lesson {

    private Context context;

    private int id;
    private String subjectName;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String theme;
    private String homework;
    private String note;
    private String pass;

    private ArrayList<Mark> marksArrayList = null;

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Lesson(Context context, int lessonId) {
        this.id = lessonId;
        this.context = context;
        loadFromDB();
    }

    public Lesson(Context context, String date, int timeId, int subjectId) {
        this.context = context;
        getLesson(date, timeId, subjectId);
    }

    private void loadFromDB() {
        LessonDBInterface db = new LessonDBInterface(context);
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
    }

    public void getLesson(String date, int timeId, int subjectId) {
        LessonDBInterface db = new LessonDBInterface(context);

        Cursor cursor = db.getLesson(date, timeId, subjectId);

        if(cursor == null) {
            setId(-1);
            return;
        }

        if (cursor.moveToFirst()) {
            do {
                setValues(cursor);
            }
            while (cursor.moveToNext());
        } else {
            setId(-1);
        }

        cursor.close();
        db.closeDB();

        marksArrayList = getMarksOfLesson();
    }

    private void setValues(Cursor cursor) {
        setId(cursor.getInt(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_ID)));
        setDate(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_DATE)));
        setHomework(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_HOMEWORK)));
        setTheme(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_THEME)));
        setColor(cursor.getInt(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_COLOR)));
        setSubjectName(cursor.getString(cursor.getColumnIndex(SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_SUBJECT_NAME)));
        setNote(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_NOTE)));
        setPass(cursor.getString(cursor.getColumnIndex(LessonDBInterface.LESSON_COLUMN_PASS)));
        setTimeStart(cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_START)));
        setTimeEnd(cursor.getString(cursor.getColumnIndex(TimeDBInterface.TIME_COLUMN_END)));
    }

    private ArrayList<Mark> getMarksOfLesson() {
        if(id == -1) {
            return null;
        }

        MarkDBInterface db = new MarkDBInterface(context);

        Cursor cursor = db.getMarks(id);
        if(cursor == null) {
            return null;
        }

        ArrayList<Mark> marks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Mark mark = new Mark();
                mark.setId(cursor.getInt(cursor.getColumnIndex(MarkDBInterface.MARK_COLUMN_ID)));
                mark.setType(cursor.getString(cursor.getColumnIndex(MarkDBInterface.MARK_COLUMN_TYPE)));
                mark.setValue(cursor.getInt(cursor.getColumnIndex(MarkDBInterface.MARK_COLUMN_VALUE)));

                marks.add(mark);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.closeDB();

        if(marks.size() == 0) {
            return null;
        } else {
            return marks;
        }
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

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public ArrayList<Mark> getMarks() {
        return marksArrayList;
    }
}
