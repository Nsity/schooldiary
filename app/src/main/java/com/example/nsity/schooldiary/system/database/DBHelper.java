package com.example.nsity.schooldiary.system.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.nsity.schooldiary.system.database.tables.LessonDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by nsity on 15.11.15.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {


    private static final String DB_NAME = "DIARY";
    private static final int DB_VERSION = 2;


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        database.execSQL(SubjectDBInterface.SUBJECT_TABLE_CREATE);
        database.execSQL(TimetableDBInterface.TIMETABLE_TABLE_CREATE);
        database.execSQL(LessonDBInterface.LESSON_TABLE_CREATE);
        database.execSQL(TimeDBInterface.TIME_TABLE_CREATE);
        database.execSQL(SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_CREATE);
        //TODO

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SubjectDBInterface.SUBJECT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TimetableDBInterface.TIMETABLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LessonDBInterface.LESSON_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TimeDBInterface.TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME);

        //TODO
        onCreate(db);
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void close() {
        super.close();
    }
}