package com.example.nsity.schooldiary.system.database.tables;

import android.content.Context;

import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;

/**
 * Created by nsity on 15.11.15.
 */
public class SubjectsClassDBInterface extends ADBWorker {

    public static final String SUBJECTS_CLASS_TABLE_NAME = "SUBJECTS_CLASS";
    public static final String SUBJECTS_CLASS_COLUMN_ID = "SUBJECTS_CLASS_ID";
    public static final String SUBJECTS_CLASS_COLUMN_SUBJECT_ID = "SUBJECT_ID";
    public static final String SUBJECTS_CLASS_COLUMN_TEACHER_ID = "TEACHER_ID";


    public static final String SUBJECTS_CLASS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + SUBJECTS_CLASS_TABLE_NAME + "("
            + SUBJECTS_CLASS_COLUMN_ID + " INTEGER PRIMARY KEY , "
            + SUBJECTS_CLASS_COLUMN_SUBJECT_ID + " INTEGER, "
            + SUBJECTS_CLASS_COLUMN_TEACHER_ID + " INTEGER "
            + ");";

    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        return 0;
    }

    public SubjectsClassDBInterface(Context context) {
        super(context);
    }
}
