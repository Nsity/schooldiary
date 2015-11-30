package com.example.nsity.schooldiary.system.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.database.ADBWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 15.11.15.
 */
public class LessonDBInterface extends ADBWorker {

    public static final String LESSON_TABLE_NAME = "LESSON";
    public static final String LESSON_COLUMN_ID = "LESSON_ID";
    public static final String LESSON_COLUMN_THEME = "LESSON_THEME";
    public static final String LESSON_COLUMN_SUBJECTS_CLASS_ID = "SUBJECTS_CLASS_ID";
    public static final String LESSON_COLUMN_HOMEWORK = "LESSON_HOMEWORK";
    public static final String LESSON_COLUMN_STATUS = "LESSON_STATUS";
    public static final String LESSON_COLUMN_TIME_ID = "TIME_ID";
    public static final String LESSON_COLUMN_DATE = "LESSON_DATE";
    public static final String LESSON_COLUMN_PASS = "LESSON_PASS";
    public static final String LESSON_COLUMN_NOTE = "LESSON_NOTE";


    public static final String LESSON_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + LESSON_TABLE_NAME + "("
            + LESSON_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + LESSON_COLUMN_HOMEWORK + " VARCHAR(5000), "
            + LESSON_COLUMN_NOTE + " VARCHAR(1000), "
            + LESSON_COLUMN_THEME + " VARCHAR(300), "
            + LESSON_COLUMN_PASS + " VARCHAR(10), "
            + LESSON_COLUMN_SUBJECTS_CLASS_ID + " INTEGER, "
            + LESSON_COLUMN_DATE + " DATE, "
            + LESSON_COLUMN_STATUS + " BOOLEAN, "
            + LESSON_COLUMN_TIME_ID + " INTEGER " + ");";


    @Override
    public int save(JSONArray objects, boolean dropAllData) {
        if (dropAllData) {
            delete(LESSON_TABLE_NAME, null, null);
        }
        return addLesson(objects);
    }


    private int addLesson(JSONArray lessons) {
        if ((CommonFunctions.StringIsNullOrEmpty(lessons.toString())) || (lessons.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> lessonsValues = new ArrayList<>();
            for (int i = 0; i < lessons.length(); i++) {
                JSONObject lesson = lessons.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put(LESSON_COLUMN_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.id)));
                cv.put(LESSON_COLUMN_HOMEWORK, CommonFunctions.getFieldString(lesson, context.getString(R.string.homework)).equals("null") ? "" :
                        CommonFunctions.getFieldString(lesson, context.getString(R.string.homework)));
                cv.put(LESSON_COLUMN_STATUS, CommonFunctions.getFieldInt(lesson, context.getString(R.string.status)));
                cv.put(LESSON_COLUMN_THEME, CommonFunctions.getFieldString(lesson, context.getString(R.string.theme)).equals("null") ? "" :
                        CommonFunctions.getFieldString(lesson, context.getString(R.string.theme)));
                cv.put(LESSON_COLUMN_TIME_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.time_id)));
                cv.put(LESSON_COLUMN_SUBJECTS_CLASS_ID, CommonFunctions.getFieldInt(lesson, context.getString(R.string.subject_id)));
                cv.put(LESSON_COLUMN_DATE, CommonFunctions.getFieldString(lesson, context.getString(R.string.date)));
                cv.put(LESSON_COLUMN_NOTE, CommonFunctions.getFieldString(lesson, context.getString(R.string.note)).equals("null") ? "" :
                        CommonFunctions.getFieldString(lesson, context.getString(R.string.note)));
                cv.put(LESSON_COLUMN_PASS, CommonFunctions.getFieldString(lesson, context.getString(R.string.pass)).equals("null") ? "" :
                        CommonFunctions.getFieldString(lesson, context.getString(R.string.pass)));

                lessonsValues.add(cv);
            }
            return insert(LESSON_TABLE_NAME, ADBWorker.REPLACE, lessonsValues);
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public LessonDBInterface(Context context) {
        super(context);
    }


    public Cursor getLesson(String id) {
        String selectQuery = "SELECT * FROM " + LESSON_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + LESSON_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " WHERE " + LESSON_COLUMN_ID + " =? ";
        return getCursor(selectQuery, new String[] {id});
    }

    public Cursor getLesson(String date, int timeId, int subjectId) {
        String selectQuery = "SELECT * FROM " + LESSON_TABLE_NAME + " t JOIN " + SubjectsClassDBInterface.SUBJECTS_CLASS_TABLE_NAME
                + " s ON t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " = s." + SubjectsClassDBInterface.SUBJECTS_CLASS_COLUMN_ID +
                " JOIN " + TimeDBInterface.TIME_TABLE_NAME + " time ON t." + LESSON_COLUMN_TIME_ID + " = time." +
                TimeDBInterface.TIME_COLUMN_ID + " WHERE " + LESSON_COLUMN_DATE + " =? AND t." +
                LESSON_COLUMN_TIME_ID + " =? AND t." + LESSON_COLUMN_SUBJECTS_CLASS_ID + " =? ";
        return getCursor(selectQuery, new String[]{date, String.valueOf(timeId), String.valueOf(subjectId)});
    }


    public void deleteLesson(int lessonId) {
        delete(LESSON_TABLE_NAME, LESSON_COLUMN_ID + " =?", new String[] {String.valueOf(lessonId)});
        delete(MarkDBInterface.MARK_TABLE_NAME, LESSON_COLUMN_ID + " =?", new String[] {String.valueOf(lessonId)});
    }
}
