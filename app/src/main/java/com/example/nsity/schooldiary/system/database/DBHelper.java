package com.example.nsity.schooldiary.system.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nsity.schooldiary.system.database.tables.LessonsDBInterface;
import com.example.nsity.schooldiary.system.database.tables.MessageDBInterface;
import com.example.nsity.schooldiary.system.database.tables.PeriodsDBInterface;
import com.example.nsity.schooldiary.system.database.tables.ProgressDBInterface;
import com.example.nsity.schooldiary.system.database.tables.SubjectsClassDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TeachersDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimeDBInterface;
import com.example.nsity.schooldiary.system.database.tables.TimetableDBInterface;

/**
 * Created by nsity on 15.11.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DIARY";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TimetableDBInterface.TIMETABLE_TABLE_CREATE);
        database.execSQL(LessonsDBInterface.LESSON_TABLE_CREATE);
        database.execSQL(TimeDBInterface.TIME_TABLE_CREATE);
        database.execSQL(SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_CREATE);
        database.execSQL(LessonsDBInterface.MARK_TABLE_CREATE);
        database.execSQL(ProgressDBInterface.PROGRESS_TABLE_CREATE);
        database.execSQL(PeriodsDBInterface.PERIOD_TABLE_CREATE);
        database.execSQL(TeachersDBInterface.TEACHER_TABLE_CREATE);
        database.execSQL(LessonsDBInterface.HOMEWORK_TABLE_CREATE);
        database.execSQL(MessageDBInterface.MESSAGE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TimetableDBInterface.TIMETABLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LessonsDBInterface.LESSON_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TimeDBInterface.TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LessonsDBInterface.MARK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProgressDBInterface.PROGRESS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PeriodsDBInterface.PERIOD_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TeachersDBInterface.TEACHER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LessonsDBInterface.HOMEWORK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MessageDBInterface.MESSAGE_TABLE_NAME);

        onCreate(db);
    }
}