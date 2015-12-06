package com.example.nsity.schooldiary.navigation.marks.subjects;

import com.example.nsity.schooldiary.navigation.marks.Mark;

/**
 * Created by nsity on 06.12.15.
 */
public class SubjectMark extends Mark {

    private int lessonId;
    private String date;

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
