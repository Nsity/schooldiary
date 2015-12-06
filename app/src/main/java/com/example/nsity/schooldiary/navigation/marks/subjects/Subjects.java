package com.example.nsity.schooldiary.navigation.marks.subjects;

import android.content.Context;

import com.example.nsity.schooldiary.navigation.Subject;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 04.12.15.
 */
public class Subjects {
    ArrayList<Subject> subjectsArrayList;
    private SubjectsClassDBInterface db;

    public Subjects(Context context) {
        this.db = new SubjectsClassDBInterface(context);
        subjectsArrayList = loadFromDB();
    }

    public ArrayList<Subject> loadFromDB() {
        return db.getSubjects();
    }

    public ArrayList<Subject> getSubjects() {
        return subjectsArrayList;
    }

    public Subject getItem(int position) {
        return subjectsArrayList.get(position);
    }
}
